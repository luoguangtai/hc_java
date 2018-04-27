package com.hc.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

public class DateUtil {
	public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String C_TIME_PATTON_DEFAULT1 = "yyyyMMdd HH:mm:ss";
	public static final String C_DATE_PATTON_DEFAULT = "yyyy-MM-dd";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String HHMMSS = "HH:mm:ss";
	public static final String FORMAT_ZH = "yyyy年MM月dd日";

	public static final long SECONDS_IN_DAY = 1000 * 3600 * 24;

	public static void main(String[] args) {
		System.out.println(getTodayAsStr());
		System.out.println(getTodayAsStr1());
		System.out.println(getNowAsStr());
		System.out.println(getNowAsStr1());
		System.out.println(getNowAsDate());
		System.out.println(getNowAsTimestamp());
		System.out.println(formatDate(getNowAsDate()));
		System.out.println(formatDate(getNowAsTimestamp()));
		System.out.println(formatTime(getNowAsDate()));
		System.out.println(formatTime(getNowAsTimestamp()));

		System.out.println("*****************");

		System.out.println(parse("2012-10-17"));
		System.out.println(parse2Timestamp("2012-10-17"));

		Date d1 = getNowAsDate();
		ThreadUtil.sleep(1000);
		Date d2 = getNowAsDate();
		System.out.println(DateUtil.daysBetween(d1, d2));
		System.out.println("*****************");
		System.out.println(firstDayOfMonth());
		System.out.println(lastDayOfMonth());

		Set<String> cddmSet = new TreeSet<String>();
		cddmSet.add("533001");
		cddmSet.add("533001");
		cddmSet.add("533001");
		cddmSet.add("533001");
		cddmSet.add("533001");
		cddmSet.add("533001");
		cddmSet.add("533001");
		cddmSet.add("533001");
		cddmSet.add("533002");
		cddmSet.add("533003");
		for (String aaa : cddmSet) {
			System.out.println("aaa====" + aaa);
		}
		// getWeekList("20161129");
		System.out.println("月份天数===" + getMonthDay(null));
		System.out.println("月份的第一天===" + getMonthFirstDay("20160911"));
		System.out.println("月份的最后一天===" + getMonthLastDay("20160911"));
	}

	/**
	 * 获取传入日期所在的一周的日期(周一是第一天)
	 * @param dateStr yyyyMMdd或yyyy-MM-dd格式日期
	 * 		  传null返回的是当周
	 * @return 
	 * @author luogt@hsit.com.cn
	 * @date 2016-10-21
	 */
	public static List<Map<String, Object>> getWeekList(String dateStr) {
		List<Map<String, Object>> weekList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);

		Date time = new Date();
		if (!ValidateUtil.isEmpty(dateStr)) {
			time = parse(dateStr);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		// 是一周中的第几天
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			dayOfWeek = 8;
		}
		cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek + 1);
		Map<String, Object> weekMap;
		for (int i = 1; i <= 7; i++) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
			String weekDay = sdf.format(cal.getTime());
			weekMap = new HashMap<String, Object>();
			String weekDayName = "星期";
			switch (i) {
			case 1:
				weekDayName = weekDayName + "一";
				break;
			case 2:
				weekDayName = weekDayName + "二";
				break;
			case 3:
				weekDayName = weekDayName + "三";
				break;
			case 4:
				weekDayName = weekDayName + "四";
				break;
			case 5:
				weekDayName = weekDayName + "五";
				break;
			case 6:
				weekDayName = weekDayName + "六";
				break;
			case 7:
				weekDayName = weekDayName + "日";
				break;
			}
			weekMap.put("weekDayName", weekDayName);
			weekMap.put("weekDay", weekDay);
			weekList.add(weekMap);
		}
		return weekList;
	}

	/**
	 * 获取传入日期所在月份的天数
	 * @param dateStr yyyyMMdd或yyyy-MM-dd格式日期
	 * 		  传null返回的是当月的天数
	 * @return
	 * @author luogt@hsit.com.cn
	 * @date 2016-10-22
	 */
	public static int getMonthDay(String dateStr) {
		Date time = new Date();
		if (!ValidateUtil.isEmpty(dateStr)) {
			time = parse(dateStr);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.DATE, 1);
		cal.roll(Calendar.DATE, -1);
		int maxDate = cal.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 获取传入日期所在月份的第一天
	 * @param dateStr yyyyMMdd或yyyy-MM-dd格式日期
	 * 		  传null返回的是当月的第一天
	 * @return
	 * @author luogt@hsit.com.cn
	 * @date 2016-10-22
	 */
	public static String getMonthFirstDay(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
		Date time = new Date();
		if (!ValidateUtil.isEmpty(dateStr)) {
			time = parse(dateStr);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return sdf.format(cal.getTime());
	}

	/**
	 * 获取传入日期所在月份的最后一天
	 * @param dateStr yyyyMMdd或yyyy-MM-dd格式日期
	 * 		  传null返回的是当月的最后一天
	 * @return
	 * @author luogt@hsit.com.cn
	 * @date 2016-10-22
	 */
	public static String getMonthLastDay(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
		Date time = new Date();
		if (!ValidateUtil.isEmpty(dateStr)) {
			time = parse(dateStr);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return sdf.format(cal.getTime());
	}

	/**
	 * 获得当前日期，格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getTodayAsStr() {
		return getCurrentDate(C_DATE_PATTON_DEFAULT);
	}

	/**
	 * 获得当前日期，格式yyyyMMdd
	 * 
	 * @return
	 */
	public static String getTodayAsStr1() {
		return getCurrentDate(YYYYMMDD);
	}

	/**
	 * 获得当前时间，格式yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static String getNowAsStr() {
		return getCurrentDate(C_TIME_PATTON_DEFAULT);
	}

	/**
	 * 获得当前时间，格式yyyyMMdd hh:mm:ss
	 * @return
	 */
	public static String getNowAsStr1() {
		return getCurrentDate(C_TIME_PATTON_DEFAULT1);
	}

	/**
	 * 取当前时间
	 * @return
	 */
	public static Date getNowAsDate() {
		return new Date();
	}

	/**
	 * 取当前时间
	 * @return
	 */
	public static Timestamp getNowAsTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 获得当前时间，格式自定义
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrentDate(String format) {
		Calendar day = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String date = sdf.format(day.getTime());
		return date;
	}

	/**
	 * 取当月的第一天
	 * @return
	 */
	public static String firstDayOfMonth() {
		return format(new Date(), "yyyy-MM-01");
	}

	/**
	 * 取当月的最后一天
	 * @return
	 */
	public static String lastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return format(new Date(), "yyyy-MM-" + maxDay);
	}

	/**
	 * 将字符串转换成Date 格式可以是以下几种：yyyy-MM-dd；yyyy-MM-dd HH:mm:ss； yyyyMMdd
	 * HH:mm:ss；yyyyMMdd；HH:mm:ss
	 * @param strDate 字符串
	 * @return date
	 */
	public static Date parse(String strDate) {
		if (StringUtils.isBlank(strDate)) {
			return null;
		}

		String pattern = C_DATE_PATTON_DEFAULT;
		if (strDate.indexOf("-") > 0 && strDate.indexOf(":") > 0) {
			pattern = C_TIME_PATTON_DEFAULT;
		} else if (strDate.indexOf("-") < 0 && strDate.indexOf(" ") > 0 && strDate.indexOf(":") > 0) {
			pattern = "yyyyMMdd HH:mm:ss";
		} else if (strDate.indexOf("-") > 0 && strDate.indexOf(":") < 0) {
			pattern = C_DATE_PATTON_DEFAULT;
		} else if (strDate.indexOf("-") < 0 && strDate.indexOf(":") < 0) {
			pattern = YYYYMMDD;
		} else if (strDate.indexOf("-") < 0 && strDate.indexOf(":") > 0) {
			pattern = HHMMSS;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date newDate = null;
		try {
			newDate = dateFormat.parse(strDate);
		} catch (ParseException pe) {
			newDate = null;
		}
		return newDate;
	}

	/**
	 * 将字符串转换成Timestamp
	 * @param dateStr 格式化的字符串
	 * @return Timestamp
	 */
	public static Timestamp parse2Timestamp(String dateStr) {
		Date date = parse(dateStr);
		if (date == null) {
			return null;
		} else {
			return new Timestamp(date.getTime());
		}
	}

	/**
	 * 将date格式化成字符串，格式2012-09-12
	 * @param date 需要格式化的日期
	 * @return 字符串，格式yyyy-MM-dd
	 */
	public static String formatDate(Date date) {
		return format(date, C_DATE_PATTON_DEFAULT);
	}

	/**
	 * 将date格式化成字符串，格式2012-09-12 14:24:23
	 * @param date 需要格式化的日期
	 * @return 字符串，格式yyyy-MM-dd HH:mm:ss
	 */
	public static String formatTime(Date date) {
		return format(date, C_TIME_PATTON_DEFAULT);
	}

	/**
	 * 将Date转化成指定格式的字符串
	 * @param date 需要格式化的日期
	 * @param pattern 格式
	 * @return 字符串
	 */
	public static String format(Date date, String pattern) {
		if (date == null || pattern == null) {
			return null;
		}

		SimpleDateFormat dateFromat = new SimpleDateFormat();
		dateFromat.applyPattern(pattern);

		return dateFromat.format(date);
	}

	/**
	 * 取得指定日期N天后的日期
	 * @param date 指定日期
	 * @param days 增加的天数，负数表示减
	 * @return date
	 */
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * @param date1 date1
	 * @param date2 date2
	 * @return int 相差的天数
	 */
	public static int daysBetween(Date date1, Date date2) {
		long between_days = (date2.getTime() - date1.getTime()) / SECONDS_IN_DAY;
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 判断字符串是否符合时间格式
	 * @param dateStr 字符串
	 * @return true or false
	 */
	public static boolean isDate(String dateStr) {
		if (StringUtils.isBlank(dateStr)) {
			return false;
		} else if (parse(dateStr) != null) {
			return true;
		} else {
			return false;
		}
	}

}
