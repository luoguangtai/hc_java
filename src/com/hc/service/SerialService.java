package com.hc.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.hc.dao.ISerialDao;
import com.hc.exception.AppException;
import com.hc.model.SerialRule;
import com.hc.sys.SpringUtil;
import com.hc.util.JsonUtil;
import com.hc.util.ValidateUtil;

public class SerialService extends BaseService {
	private static ISerialDao serialDao = (ISerialDao) SpringUtil.getBean("com.hc.dao.SerialDao");

	public static String getSerialCd(String serialEk, String orgCd) {
		String serialCdArr[] = getSerialCdS(serialEk, null, 1, orgCd);
		return serialCdArr[0];
	}

	public static String[] getSerialCdS(String serialEk, Map<String, String> dataMap, int cdNum, String orgCd) {
		List<Map> serialRuleList = serialDao.getSerialRuleList(serialEk, orgCd);
		if (serialRuleList.size() != 1) {
			throw new AppException("(" + serialEk + ")未配置对应流水号生成规则或配置重复");
		}
		Map serialRuleMap = serialRuleList.get(0);
		List<SerialRule> ruleList = (List<SerialRule>) JsonUtil.toList(serialRuleMap.get("serialRule").toString(), SerialRule.class);
		int len = 0;
		for (int i = 0; i < ruleList.size(); i++) {
			SerialRule serialRule = (SerialRule) ruleList.get(i);
			if (serialRule.getLen() > 0)
				len = serialRule.getLen();
		}
		if (len == 0) {
			throw new AppException("未配置流水号长度代码len");
		}
		// 获取流水号前缀
		String prefixCd = createPrefixCd(ruleList, dataMap);
		// 获取流水号数组
		String serialCdArr[] = getBatchFixedSerialCd(prefixCd, serialEk, len, orgCd, cdNum);
		return serialCdArr;
	}

	/**
	 * 根据流水号规则创建流水号前缀
	 * @param rules		流水号规则
	 * @param dataMap 	数据源
	 * @return
	 */
	private static String createPrefixCd(List<SerialRule> ruleList, Map<String, String> dataMap) {
		String prefixCd = "";
		// 获取User对象属性列表
		for (int i = 0; i < ruleList.size(); i++) {
			SerialRule serialRule = ruleList.get(i);
			prefixCd += serialRuleProcess(serialRule, dataMap);
		}
		return prefixCd;
	}

	/**
	 * 根据配置规则获取对应数据
	 * @param serialRule	流水号规则
	 * @param dataMap 		数据源
	 * @return
	 */
	private static String serialRuleProcess(SerialRule serialRule, Map<String, String> dataMap) {
		try {
			String value = "";
			// 未配置fill,默认使用‘0’补全
			String fill = ValidateUtil.isEmpty(serialRule.getFill()) ? "0" : serialRule.getFill();
			// 如果key==SerialNo将用%s替代，当从数据库获取流水号后将被替换
			if ("SerialNo".equalsIgnoreCase(serialRule.getKey())) {
				return "%s";
			}
			// 如果数据源Map中有数据，则从dataMap中获取
			else if (dataMap != null && dataMap.containsKey(serialRule.getKey())) {
				value = dataMap.get(serialRule.getKey());
			}
			// 年
			else if ("YYYY".equalsIgnoreCase(serialRule.getKey())) {
				String year = String.valueOf((Calendar.getInstance().get(Calendar.YEAR)));
				return year;
			}
			// 年
			else if ("YY".equalsIgnoreCase(serialRule.getKey())) {
				String year = String.valueOf((Calendar.getInstance().get(Calendar.YEAR)));
				return year.substring(year.length() - 2, year.length());
			}
			// 月
			else if ("MM".equalsIgnoreCase(serialRule.getKey())) {
				String month = String.valueOf((Calendar.getInstance().get(Calendar.MONTH) + 1));
				return month.length() < 2 ? "0" + month : month;
			}
			// 日
			else if ("DD".equalsIgnoreCase(serialRule.getKey())) {
				String day = String.valueOf((Calendar.getInstance().get(Calendar.DATE)));
				return day.length() < 2 ? "0" + day : day;
			}
			// 年月日
			else if ("YYMMDD".equalsIgnoreCase(serialRule.getKey())) {
				Calendar ca = Calendar.getInstance();
				String year = String.valueOf((ca.get(Calendar.YEAR)));
				String month = String.valueOf((ca.get(Calendar.MONTH) + 1));
				String day = String.valueOf(ca.get(Calendar.DATE));
				String yymmdd = year.substring(year.length() - 2, year.length()) + (month.length() < 2 ? "0" + month : month)
						+ (day.length() < 2 ? "0" + day : day);
				return yymmdd;
			}
			// 年月日
			else if ("YYYYMMDD".equalsIgnoreCase(serialRule.getKey())) {
				Calendar ca = Calendar.getInstance();
				String year = String.valueOf((ca.get(Calendar.YEAR)));
				String month = String.valueOf((ca.get(Calendar.MONTH) + 1));
				String day = String.valueOf(ca.get(Calendar.DATE));
				return year + (month.length() < 2 ? "0" + month : month) + (day.length() < 2 ? "0" + day : day);
			}
			// 否则key视为固定字符
			else {
				value = serialRule.getKey();
			}

			// 向后截取
			if (serialRule.getLastLen() > 0) {
				return substring(serialRule.getLastLen(), value, fill, false);
			}
			// 向前截取
			else {
				return substring(serialRule.getFirstLen(), value, fill, true);
			}
		} catch (Exception e) {
			throw new AppException("规则取数报错");
		}
	}

	private static String[] getBatchFixedSerialCd(String prefixCd, String serialEk, int len, String orgCd, int cdNum) {
		String stringPrefixCd = prefixCd.replaceAll("%s", "");
		String serialCd[] = createBatchSearialCd(serialEk, stringPrefixCd, orgCd, len, cdNum);
		for (int i = 0; i < serialCd.length; i++) {
			serialCd[i] = String.format(prefixCd, serialCd[i]);
		}
		return serialCd;
	}

	public static String[] createBatchSearialCd(String serialEk, String prefixCd, String orgCd, int fixedDigit, int cdNum) {
		long startSerialNum = serialDao.getSerialNum(serialEk, prefixCd, cdNum, orgCd);
		String[] serialCds = new String[cdNum];
		for (int i = 0; i < serialCds.length; i++) {
			serialCds[i] = getFixedString(startSerialNum, fixedDigit);
			startSerialNum++;
		}
		return serialCds;
	}

	/**
	 * 数据截取
	 * @param subLen	截取长度
	 * @param value		数据
	 * @param fill		长度不足时填充符
	 * @param isFirst	是否从前面截取
	 * @return
	 */
	private static String substring(int subLen, String value, String fill, boolean isFirst) {
		try {
			if (subLen <= 0) {
				return value;
			}
			String str = "";
			if (value.length() > subLen) {
				str = isFirst ? value.substring(0, subLen) : value.substring(value.length() - subLen);
			} else {
				str = value;
			}
			for (int i = value.length(); i < subLen; i++) {
				str += fill;
			}
			return str;
		} catch (Exception e) {
			throw new AppException("字符串截取错误");
		}
	}

	/**
	 * 获取固定位数的字符串
	 * @param searialNo 流水号
	 * @param fixedDigit 固定位数
	 * @return String
	 */
	private static String getFixedString(long searialNo, int fixedDigit) {
		// 格式化流水号
		String stringSearialNo = String.valueOf(searialNo);
		if (stringSearialNo.length() < fixedDigit) {
			for (int i = stringSearialNo.length(); i < fixedDigit; i++) {
				stringSearialNo = "0" + stringSearialNo;
			}
		}
		return stringSearialNo;
	}
}
