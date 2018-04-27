package com.hc.util;

/**
 * 类型转换类
 * 
 */
public class ConvertUtil {

	/**
	 * 数组转化成字符串，用"[]"分开
	 * @param values String[]
	 * @return String
	 */
	public static String toString(String[] values) {
		String value = "";
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				value += "[" + values[i] + "]";
			}
		}
		return value;
	}

	/**
	 * 数字数组转化成字符串，用"[]"分开
	 * @param values String[]
	 * @return String
	 */
	public static String toString(long[] values) {
		String value = "";
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				value += "[" + values[i] + "]";
			}
		}
		return value;
	}

	/**
	 * 整数数组转化成字符串，用"[]"分开
	 * @param values String[]
	 * @return String
	 */
	public static String toString(int[] values) {
		String value = "";
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				value += "[" + values[i] + "]";
			}
		}
		return value;
	}

	/**
	 * []型字符串转换成数字数组
	 * @param parameter String
	 * @return String
	 */
	public static long[] toLongArray(String parameter) {
		long[] value = new long[0];
		if (parameter == null || parameter.length() < 2)
			return value;

		try {
			String[] strArray = parameter.substring(1, parameter.length() - 1).split("\\]\\[");
			if (strArray == null) {
				return value;
			}
			value = new long[strArray.length];
			for (int i = 0; i < strArray.length; i++) {
				try {
					value[i] = Long.parseLong(strArray[i]);
				} catch (NumberFormatException ex1) {
					value[i] = 0;
				}
			}
			return value;
		} catch (Exception ex) {
			return value;
		}
	}

	/**
	 * []型字符串转化成整数数组
	 * @param parameter String
	 * @return String
	 */
	public static int[] toIntArray(String parameter) {
		int[] value = new int[0];
		if (parameter == null || parameter.length() < 2)
			return value;

		try {
			String[] strArray = parameter.substring(1, parameter.length() - 1).split("\\]\\[");
			if (strArray == null) {
				return value;
			}
			value = new int[strArray.length];
			for (int i = 0; i < strArray.length; i++) {
				try {
					value[i] = Integer.parseInt(strArray[i]);
				} catch (NumberFormatException ex1) {
					value[i] = 0;
				}
			}
			return value;
		} catch (Exception ex) {
			return value;
		}
	}

	/**
	 * []型字符串转化成字符串数组
	 * @param parameter String
	 * @return String
	 */
	public static String[] toStringArray(String parameter) {
		String[] value = new String[0];
		if (parameter == null || parameter.length() < 2)
			return value;
		if (parameter.indexOf("[") < 0) {
			String[] ret = { parameter };
			return ret;
		}

		try {
			String[] strArray = parameter.substring(1, parameter.length() - 1).split("\\]\\[");
			return strArray;
		} catch (Exception ex) {
			return value;
		}
	}

	/**
	 * 字符型数据格式化，在参数上增加左右两个“'”
	 * @param str 一个字符串
	 * @return
	 */
	public static String formatStrForDB(String str) {
		return String.format("'%s'", str);
	}

	/**
	 * 将字符串数据，转换成数据库sql或hql的in 函数 如 strArr =["a","b","c"]，转成 “('a','b','c')”
	 * @param values
	 * @return
	 */
	public static String toDbString(String[] values) {
		String text = "(";
		for (int i = 0; i < values.length; i++) {
			if (i == 0)
				text += "'" + values[i] + "'";
			else
				text += ",'" + values[i] + "'";
		}
		text += ")";
		return text;
	}

}
