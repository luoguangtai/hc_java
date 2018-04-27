package com.hc.util;

public class FileUtil {
	/**
	 * 获取文件扩展名
	 * @param s 文件名包括后缀 
	 * @param split 文件名和后缀之间的‘.’ 
	 * @return 文件扩展名
	 * @author luogt@hsit.com.cn
	 * @date 2016-12-14
	 */
	public static String getExtName(String s, String split) {
		int i = s.lastIndexOf(split);
		int leg = s.length();
		return i > 0 ? (i + 1) == leg ? " " : s.substring(i + 1, s.length()) : " ";
	}

	public static void main(String[] args) {
		System.out.println(getExtName("55.png", "."));
	}
}
