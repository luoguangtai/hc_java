package com.hc.util;

import java.util.UUID;

public class UUIDUtil {
	/**
	 * 返回32位的UUID，所有字母转成大写
	 * @return
	 */
	public static String randomUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	public static void main(String[] args) {
		System.out.println(randomUUID());
	}
}
