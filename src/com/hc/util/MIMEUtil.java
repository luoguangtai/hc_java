package com.hc.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class MIMEUtil {
    private static final String MIME_APPLICATION_OCTET_STREAM = "application/octet-stream";
    private static final Map<String, String> MIME_MAP = new HashMap<String, String>();

    //读取配置文件
    static {
        InputStream in = MIMEUtil.class.getResourceAsStream("MIME.properties");
        Properties p = new Properties();
        try {
            p.load(in);
            Enumeration<?> en = p.propertyNames();
            while(en.hasMoreElements()){
                String key = (String)en.nextElement();
                MIME_MAP.put(key.toLowerCase(), p.getProperty(key).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try{
                in.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 根据文件后缀名获得对应的MIME类型,用在文件下载时response.setContentType（）的值
     * @param filetype
     * @return
     */
    public static String getMIMEType(String filetype) {
        String in_str = filetype.toLowerCase();
        if(in_str.startsWith(".")){
            in_str = in_str.substring(1);
        }
        return MapUtil.getString(MIME_MAP, in_str, MIME_APPLICATION_OCTET_STREAM);
    }
    
    public static void main(String[] args){
        System.out.println(getMIMEType(".doc"));
    }
}
