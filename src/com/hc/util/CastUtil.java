package com.hc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author chensh
 */
public class CastUtil {
	static final String TAB = "    ";

	public static List castList(List list) {
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> rtMap = castMap(list.get(i));
			list.set(i, rtMap);
		}
		return list;
	}

	public static Map<String, Object> castMap(Object obj) {
		if (obj == null) {
			return null;
		}
		Map<String, Object> rtMap = new HashMap<String, Object>();

		Map mpMap = (Map) obj;
		Set keySet = mpMap.keySet();
		for (Object key : keySet) {
			String cameName = filedNameToJavaName(key.toString());
			rtMap.put(cameName, mpMap.get(key));
		}
		return rtMap;
	}

	// 转换到文件
	public static void toFile(String path, String fileName, String value) {
		try {
			// 先判断是否有src目录，如果没有则创建src目录
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// 用UTF8输出文件
			FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			out.write(value);
			out.flush();
			out.close();
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static boolean isBlank(String v) {
		if (v == null || v.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String firstUpper(String s) {
		String lowStr = s.toLowerCase();
		return lowStr.substring(0, 1).toUpperCase() + lowStr.substring(1);
	}

	public static String tableNameToClassName(String tablename) {
		String[] sa = tablename.split("[_]");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < sa.length; ++i) {
			sb.append(firstUpper(sa[i]));
		}

		return sb.toString();
	}

	public static String filedNameToJavaName(String fileName) {
		String t = tableNameToClassName(fileName);
		return t.substring(0, 1).toLowerCase() + t.substring(1);
	}

	/**
	 * 判断String数组中是否包含某个字符串
	 * @param stringArray
	 * @param source
	 * @return true包含，false不包含
	 */
	public static boolean contains(String[] stringArray, String source) {
		// 转换为list
		List<String> tempList = Arrays.asList(stringArray);

		// 利用list的包含方法，进行判断
		if (tempList.contains(source)) {
			return true;
		}
		return false;
	}

	public static void main(String args[]) {
		System.out.println(tableNameToClassName("aaaa_bb_cc_dd_9992366"));
		System.out.println(filedNameToJavaName("y_aaa_bb_cc_dd_9992366"));
		String root = "D:\\aa\\bb";
		String pkg = "com.hs.tlds";
		// System.out.println(root + File.separator + pkg.replaceAll("\\.",
		// "\\"+File.separator));
	}
}
