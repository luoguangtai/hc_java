package com.hc.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.hc.util.MapUtil;

/**
 * 读取配置文件dp-config.properties
 * @author chen
 */
public class Config {
	// 配置文件名,放在WEB-INF/classes/下
	private static final String CONFIG_FILENAME = "sys-config.properties";
	// 配置文件的最后修改时间
	private static long lastModifiedTime = -1;

	// 上传文件的最大尺寸（单位：M）
	private static int fileUploadMaxSize = 10;
	// 是否trim所有Request的Parameter
	private static boolean trimRequestParameter = true;
	// jsp路径
	private static String pagePath = "/sys/";
	// batch size
	private static int batchSize = 500;
	// gzip enabled
	private static boolean gzipEnabled = true;
	// gzip threshold
	private static int gzipMinSize = 2048;

	private static Map<String, String> config_map = new HashMap<String, String>();

	private static void reload() {

		URL url = Config.class.getClassLoader().getResource(CONFIG_FILENAME);
		if (url == null) {
			throw new RuntimeException("没找到配置文件:" + CONFIG_FILENAME);
		}
		// 配置文件的最后修改时间
		File file = new File(url.getFile());
		if (!file.exists()) {
			throw new RuntimeException("没找到配置文件:" + CONFIG_FILENAME);
		}
		long modifiedTime = file.lastModified();
		// 发生过修改则重新加载配置文件
		if (modifiedTime != lastModifiedTime) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				Properties p = new Properties();
				p.load(in);
				Enumeration<?> en = p.propertyNames();
				while (en.hasMoreElements()) {
					String key = (String) en.nextElement();
					config_map.put(key, p.getProperty(key).trim());
				}
				fileUploadMaxSize = MapUtil.getInteger(config_map, "FileUploadMaxSize", 10);
				trimRequestParameter = MapUtil.getBoolean(config_map, "TrimRequestParameter", true);
				batchSize = MapUtil.getInteger(config_map, "BatchSize", 500);
				;
				pagePath = config_map.get("PagePath");
				if (pagePath != null && !pagePath.endsWith("/")) {
					pagePath += "/";
				}
				gzipEnabled = MapUtil.getBoolean(config_map, "GZIPEnabled", true);
				gzipMinSize = MapUtil.getInteger(config_map, "GZIPMinSize", 2048);
				// 记录最后修改时间
				lastModifiedTime = modifiedTime;
			} catch (Exception e) {
				throw new RuntimeException("配置文件" + CONFIG_FILENAME + "加载错误:" + e.getMessage());
			} finally {
				// 关闭inputstream
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {
					}
				}
			}
		}
	}

	public static int getFileUploadMaxSize() {
		reload();
		return fileUploadMaxSize;
	}

	public static boolean isTrimRequestParameter() {
		reload();
		return trimRequestParameter;
	}

	public static String getPagePath() {
		reload();
		return pagePath;
	}

	public static int getBatchSize() {
		reload();
		return batchSize;
	}

	public static boolean isGzipEnabled() {
		reload();
		return gzipEnabled;
	}

	public static int getGzipMinSize() {
		reload();
		return gzipMinSize;
	}

	public static String getConfigValue(String key) {
		reload();
		return config_map.get(key);
	}

	public static Map<String, String> getConfigMap() {
		reload();
		return config_map;
	}

	public static void main(String[] args) {
		String separator = System.getProperty("file.separator");
		System.out.println(separator);
	}
	// Properties p = new Properties();
	// // 获得文件系统分隔符
	// String spa = System.getProperty("file.separator");
	// // 通过绝对路径获得文件然后获得流
	// String path5=realpath + spa + "WEB-INF" + spa +"classes" + spa +
	// "config.properties";
	// System.out.println(path5);
}
