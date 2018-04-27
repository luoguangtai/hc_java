package com.hc.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;

public class MoneyUtil {
	private static final String[] NUMBERS = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	/** 整数部分的单位 */
	private static final String[] IUNIT = { "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰","仟" };

	/** 小数部分的单位 */
	private static final String[] DUNIT = { "角", "分", "厘" };
	
	/** 负数 */
	private static final String  NEGATIVE= "负";
	
	/** 零 */
	private static final String  ZERO= "零";

	/** 整数部分长度限制，仟万亿级 */

	private static int LIMIT = 16;

	/**
	 * 得到中文金额。
	 * @return 中文金额
	 */
	public static String toChinese(String str) {
		boolean isNegative = false;
		String returnStr = StringUtils.EMPTY;
		// 去掉","
		String number = getFormatNum(str);
		// 不是金额，直接返回
		if (!isMoney(number)) {
			return number;
		}
		//判断是否为负数
		if(number.indexOf("-")!=-1){
			number = number.substring(1, str.length());
			isNegative = true;
		}
		//判断是否为零
		if("0".equals(number)){
			return ZERO;
		}
		// 整数部分数字
		String integerStr;
		// 小数部分数字
		String decimalStr;

		// 初始化：格式数字；分离整数部分和小数部分；确定长度
		if (number.indexOf(".") > 0) {
			integerStr = number.substring(0, number.indexOf("."));
			decimalStr = number.substring(number.indexOf(".") + 1);
		}
		else if (number.indexOf(".") == 0) {
			integerStr = "";
			decimalStr = number.substring(1);
		}
		else {
			integerStr = number;
			decimalStr = "";
		}

		// integerStr去掉首0，不必去掉decimalStr的尾0(超出部分舍去)
		if (!integerStr.equals("")) {
			integerStr = Long.toString(Long.parseLong(integerStr));
			if (integerStr.equals("0")) {
				integerStr = "";
			}
		}

		// overflow超出处理能力，直接返回
		if (integerStr.length() > LIMIT) {
			return number;
		}
		// 整数部分数字
		int[] integers = toArray(integerStr);
		// 设置万单位
		boolean isMust5 = isMust5(integerStr);
		// 小数部分数字
		int[] decimals = toArray(decimalStr);

		// 返回大写金额
		returnStr = getChineseInteger(integers, isMust5) + getChineseDecimal(decimals);
		return isNegative ? NEGATIVE + returnStr : returnStr;
	}

	/**
	 * 整数部分和小数部分转换为数组，从高位至低位
	 */
	private static int[] toArray(String number) {
		int[] array = new int[number.length()];
		for (int i = 0; i < number.length(); i++) {
			array[i] = Integer.parseInt(number.substring(i, i + 1));
		}
		return array;
	}

	/**
	 * 得到中文金额的整数部分。
	 * @return 整数部分
	 */
	private static String getChineseInteger(int[] integers, boolean isMust5) {
		StringBuffer chineseInteger = new StringBuffer("");
		String key = "";
		int length = integers.length;
		for (int ii = 0; ii < length; ii++) {
			if (integers[ii] == 0) {
				// 万(亿)(必填)
				if ((length - ii) == 13){
					key = IUNIT[4];
				}
				// 亿(必填)
				else if ((length - ii) == 9){
					key = IUNIT[8];
				}
				// 万(不必填)
				else if ((length - ii) == 5 && isMust5){
					key = IUNIT[4];
				}
				// 元(必填)
				else if ((length - ii) == 1){
					key = IUNIT[0];
				}
				// 0遇非0时补零，不包含最后一位
				if ((length - ii) > 1 && integers[ii + 1] != 0){
					key += NUMBERS[0];
				}
				// 其余情况
				else{
					key += "";
				}
				
			}
			chineseInteger.append(integers[ii] == 0 ? key : (NUMBERS[integers[ii]] + IUNIT[length - ii - 1]));
			key = "";
		}
		return chineseInteger.toString();
	}

	/**
	 * 得到中文金额的小数部分。
	 * @param decimals decimals
	 * @return 小数部分
	 */
	private static String getChineseDecimal(int[] decimals) {
		StringBuffer chineseDecimal = new StringBuffer("");
		for (int ii = 0; ii < decimals.length; ii++) {
			// 舍去3位小数之后的
			if (ii == 3){
				break;
			}
			chineseDecimal.append(decimals[ii] == 0 ? "" : (NUMBERS[decimals[ii]] + DUNIT[ii]));
		}
		return chineseDecimal.toString();
	}

	/**
	 * 格式化数字：去掉","。
	 * @param num 数字
	 * @return 格式化后的数字
	 */
	private static String getFormatNum(String number) {
		return number.replaceAll(",", "");
	}

	/**
	 * 判断第5位数字的单位"万"是否应加。
	 * @return true是false否
	 */
	private static boolean isMust5(String integerStr) {
		int length = integerStr.length();
		if (length > 4) {
			String subInteger = "";
			if (length > 8) {
				// 取得从低位数，第5到第8位的字串
				subInteger = integerStr.substring(length - 8, length - 4);
			}
			else {
				subInteger = integerStr.substring(0, length - 4);
			}
			return Integer.parseInt(subInteger) > 0;
		}
		else {
			return false;
		}
	}

	/**
	 * 判断金额字符串是否合法 不合法：a.包含多于一个"."；b.包含"."、","和数字以外的字符；c.￥/$?。
	 * @return true是false否
	 */
	private static boolean isMoney(String number) {
		try {
			Double.parseDouble(number);
		}
		catch (Exception ex) {
			return false;
		}
		return true;
	}
	/**
	 * 格式化金额字符串。如输入 1210.50 格式化输出为￥1,210.50
	 * @param str 字符串1210.50
	 * @return 字符串￥1,210.50
	 */   
	public static String getCurrency(String str) {   
		NumberFormat n = NumberFormat.getCurrencyInstance();   
	    double d;   
	    String outStr = null;   
	    try {   
	    	d = Double.parseDouble(str);   
	        outStr = n.format(d);   
	    } catch (NumberFormatException e) {   
	    	throw e;
	    }   
	    return outStr;   
	}   
	
	/**
	 * 格式化金额字符串。如输入 1210.50 格式化输出为 1,210.50
	 * @param str 字符串 1210.50 
	 * @return 1,210.50
	 */   
	public static String getGeneral(String str) {   
		NumberFormat n = NumberFormat.getInstance();   
		n.setMinimumFractionDigits(2);
		double d;   
		String outStr = null;   
		try {   
			d = Double.parseDouble(StringUtils.isBlank(str) ? "0" : str);   
			outStr = n.format(d);   
		} catch (NumberFormatException e) {   
			throw e;
		}   
		return outStr;   
	}   
	
	/**
	 * 格式化金额字符串。如输入 1210.50 格式化输出为￥1,210.50
	 * @param bigDecimal 数字
	 * @return 字符串￥1,210.50
	 */   
	public static String getCurrency(BigDecimal bigDecimal) {   
		NumberFormat n = NumberFormat.getCurrencyInstance();   
	    String outStr = n.format(bigDecimal.doubleValue());   
	    return outStr;   
	}   
	
	/**
	 * 格式化金额字符串。如输入 1210.50 格式化输出为1,210.50
	 * @param bigDecimal 数字
	 * @return 字符串1,210.50
	 */   
	public static String getGeneral(BigDecimal bigDecimal) {   
		NumberFormat n = NumberFormat.getInstance();   
		n.setMinimumFractionDigits(2);
		String outStr = n.format(bigDecimal.doubleValue());   
		return outStr;   
	}   
	
	/**
	 * 测试
	 * @param args 参数
	 */
	public static void main(String[] args){
		System.out.println(MoneyUtil.getGeneral("12332.32"));
		System.out.println(MoneyUtil.getCurrency("12332.32"));
		System.out.println(MoneyUtil.toChinese("11112132342332.32"));
	}
	
}
