package com.hc.util;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mvel2.DataConversion;
import org.mvel2.MVEL;
import org.mvel2.util.PropertyTools;

import com.hc.util.mvel.DateCH;
import com.hc.util.mvel.TimestampCH;



public final class BeanUtil {

    static {
        DataConversion.addConversionHandler(Date.class, new DateCH());
        DataConversion.addConversionHandler(Timestamp.class, new TimestampCH());
    }
    
    /**
     * 判断属性是否为空
     * @param properties
     * @return
     */
    private static boolean isNotBlank(Object properties){
    	if(properties == null){
    		return false;
    	}
    	else if(properties instanceof String){
    		return StringUtils.isNotBlank((String)properties);
    	}
    	else{
    		return true;
    	}
    }
    
    /**
     * 将第一个Object的属性拷贝到第二个对象
     * @param fromObj
     * @param toObj
     * @param cover 是否覆盖toObj的值，如果为true，当fromObj的属性为空也会替换toObj的值；
     * 如果为false，当fromObj的属性为空时不替换toObj的值
     */
    public static void copyProperties(Object fromObj, Object toObj, boolean cover){
    	if(fromObj==null || toObj==null){
    		return;
    	}
    	//fromObj是Map
    	if(fromObj instanceof Map){
    		Map from = (Map)fromObj;
    		// 循环设置
            Iterator iterator = from.entrySet().iterator();
            if(toObj instanceof Map){
            	Map to = (Map)toObj;
            	while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    if(cover || isNotBlank(entry.getValue())){
                    	to.put((String) entry.getKey(), entry.getValue());
                    }
                }
            }
            else{
            	while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    if(cover || isNotBlank(entry.getValue())){
                    	BeanUtil.setProperty(toObj, (String) entry.getKey(), entry.getValue());
                    }
                }
            }
    	}
    	else{
    		Field[] fields = fromObj.getClass().getDeclaredFields();
    		if(toObj instanceof Map){
    			Map to = (Map)toObj;
    			for (int i = 0; i < fields.length; i++) {
    				Object property = BeanUtil.getProperty(fromObj, fields[i].getName());
    				if(cover || isNotBlank(property)){
    					to.put(fields[i].getName(), property);
    				}
                }
    		}
    		else{
    			for (int i = 0; i < fields.length; i++) {
                    if (PropertyTools.hasGetter(fields[i])) {
                    	Object property = BeanUtil.getProperty(fromObj, fields[i].getName());
                    	if(cover || isNotBlank(property)){
                    		BeanUtil.setProperty(toObj, fields[i].getName(), BeanUtil.getProperty(fromObj, fields[i].getName()));
                    	}
                    }
                }
    		}
    	}
    }
    
    /**
     * 把存放Bean的List转换到存放Map的List
     * @param list
     * @return
     */
    public static List<Map> listBean2listMap(List list){
        List<Map> rs = new ArrayList<Map>(list.size());
        for(Object obj:list){
            rs.add(BeanUtil.bean2map(obj));
        }
        return rs;
    }
    
    /**
     * 把存放Map的List转换到存放Bean的List
     * @param list
     * @return
     */
    public static <T> List<T> listMap2listBean(List<Map> list, Class<T> c){
        List<T> rs = new ArrayList<T>(list.size());
        for(Map obj:list){
            rs.add(BeanUtil.map2bean(obj, c));
        }
        return rs;
    }

    /**
     * 把Bean的属性复制到Map
     * @param bean 需要复制的bean
     * @return
     */
    public static Map bean2map(Object bean) {
        if(bean == null){
            return null;
        }
        else if(bean instanceof Map){
            return (Map)bean;
        }
        else{
            Map<String, Object> m = new HashMap<String, Object>();
            Field[] fields = bean.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (PropertyTools.hasGetter(fields[i])) {
                    m.put(fields[i].getName(), MVEL.getProperty(fields[i].getName(), bean));
                }
            }
            return m;
        }
    }

    /**
     * 把Map里面的值复制到Bean
     * @param properties 源
     * @param c bean类型
     * @return 复制好的bean
     */
    public static <T> T map2bean(Map properties, Class<T> c) {
        if (properties == null) {
            return null;
        }

        try {
            T bean = c.newInstance();
            // 循环设置
            Iterator iterator = properties.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                // 设置属性
                BeanUtil.setProperty(bean, (String) entry.getKey(), entry.getValue());
            }
            return bean;
        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 从javabean读取属性
     * @param bean 读取的源
     * @param expression 属性名
     * @return Object
     */
    public static Object getProperty(Object bean, String expression){
    	if(bean==null){
    		return null;
    	}
    	if(PropertyTools.getGetter(bean.getClass(), expression)!=null){
    		return MVEL.getProperty(expression, bean);
    	}
    	else{
    		return null;
    	}
    }

    /**
     * 对javabean设置属性
     * @param bean 需要设置的bean
     * @param expression 属性名
     * @param properties 属性值
     */
    public static void setProperty(Object bean, String expression, Object properties) {
//        if(PropertyTools.getSetter(bean.getClass(), expression)!=null){
    	//PropertyTools.getSetter(bean.getClass(), expression)有bug，当出现xName字段（第二个字母大写）的情况下，返回null
    	if(PropertyTools.getSetter(bean.getClass(), expression, null)!=null){
    		if(properties!=null){
	            // set的参数
	            Class paramType = PropertyTools.getSetter(bean.getClass(), expression, null).getParameterTypes()[0];
	            // 如果value是Array，set的参数不是Array，则取第一个
	            if (properties.getClass().isArray() && !paramType.isArray()) {
	                Object[] param = (Object[]) properties;
	                properties = param[0];
	            } 
	            // 如果输入不是Array，set的参数是Array，则创建Array
	            else if (!properties.getClass().isArray() && paramType.isArray()) {
	                Object[] param = {properties};
	                properties = param;
	            }
    		}
            MVEL.setProperty(bean, expression, properties);
        }
    }

    /**
     * 给javabean赋值
     * @param bean
     * @param expression
     * @param properties
     */
//    private void setProperty_back(Object bean, String expression, Object properties) {
//        String[] ex = expression.split("\\.");
//
//        // 如果没有相应的set方法则退出
//        Class currClass = bean.getClass();
//        for (int i = 0; i < ex.length; i++) {
//            if (PropertyTools.getSetter(currClass, ex[i]) == null) {
//                return;
//            } else {
//                currClass = PropertyTools.getGetter(currClass, ex[i]).getReturnType();
//            }
//        }
//
//        // 先循环创建Object，再设置value
//        Object currObj = bean;
//        try {
//            for (int i = 0; i < ex.length - 1; i++) {
//                if (MVEL.getProperty(ex[i], currObj) == null) {
//                    Object obj = PropertyTools.getSetter(currObj.getClass(), ex[i]).getParameterTypes()[0].newInstance();
//                    MVEL.setProperty(currObj, ex[i], obj);
//                }
//                currObj = MVEL.getProperty(ex[i], currObj);
//            }
//
//            if (properties != null) {
//                // set的参数
//                Class paramType = PropertyTools.getSetter(currObj.getClass(), ex[ex.length - 1]).getParameterTypes()[0];
//                // 如果value是Array，set的参数不是Array，则取第一个
//                if (properties.getClass().isArray() && !paramType.isArray()) {
//                    Object[] param = (Object[]) properties;
//                    properties = param[0];
//                } // 如果输入不是Array，set的参数是Array，则创建Array
//                else if (!properties.getClass().isArray() && paramType.isArray()) {
//                    Object[] param = {properties};
//                    properties = param;
//                }
//            }
//            MVEL.setProperty(currObj, ex[ex.length - 1], properties);
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
    
    /**
     * 输入字符串，将第一个字母变成大写，通常用在类名
     * @param s 字符串
     * @return
     */
    public static String firstUpper(String s) {
    	String lowStr = s.toLowerCase();
    	return lowStr.substring(0, 1).toUpperCase() + lowStr.substring(1);
    }

    /**
     * 数据库表名转换成类名，去掉下划线，比如表名USER_INFO，类名为UserInfo
     * @param tablename
     * @return
     */
    public static String tableNameToClassName(String tablename) {
        String[] sa = tablename.split("[_]");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < sa.length; ++i) {
            sb.append(firstUpper(sa[i]));
        }

        return sb.toString();
    }

    /**
     * 表字段名转换java的属性，去掉下划线，比如字段名USER_NAME，属性名为userName
     * @param fileName
     * @return
     */
    public static String filedNameToJavaName(String fileName){
        String t = tableNameToClassName(fileName);
        return t.substring(0, 1).toLowerCase() + t.substring(1);
    }
    
}
