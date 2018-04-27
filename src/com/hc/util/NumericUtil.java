package com.hc.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

public class NumericUtil {
	private static final int DEF_DIV_SCALE = 10;
    /**
     * 两个Double数相加
     * @param v1
     * @param v2
     * @return double
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    
    /**
     * 两个Double数相减
     * @param v1
     * @param v2
     * @return double
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    
    /**
     * 两个Double数相乘
     * @param v1
     * @param v2
     * @return Double
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    
    /**
     * 两个Double数相除
     * @param v1
     * @param v2
     * @return Double
     */
    public static double div(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,DEF_DIV_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 两个Double数相除，并保留scale位小数
     * @param v1
     * @param v2
     * @param scale
     * @return double
     */
    public static double div(double v1,double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 返回两个数里比较大的值
     * @param v1
     * @param v2
     * @return double
     */
    public static double max(double v1, double v2){
    	BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.max(b2).doubleValue();
    }
    
    /**
     * 返回两个数里比较小的值
     * @param v1
     * @param v2
     * @return double
     */
    public static double min(double v1, double v2){
    	BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.min(b2).doubleValue();
    }
    
    /**
     * 对double数据取精度，例输入1.666666，3，返回1.667
     * @param v
     * @param scale
     * @return
     */
    public static double round(double v, int scale){
		BigDecimal b = new BigDecimal(Double.toString(v));
		b = b.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return b.doubleValue();
	}
    
    /**
     * 取余数，输入55.5，40，2，返回15.5
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static double remainder(double v1,double v2, int scale){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.remainder(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 字符串转换为double,如果字符串为空则返回默认值
     * @param v
     * @return
     */
    public static double parseDouble(String text, double defval){
    	if(StringUtils.isBlank(text)){
    		return defval;
    	}
    	text = StringUtils.trim(text);
    	return Double.parseDouble(text);
    }
    
    /**
     * 字符串转换为float,如果字符串为空则返回默认值
     * @param v
     * @return
     */
    public static float parseFloat(String text, float defval){
    	if(StringUtils.isBlank(text)){
    		return defval;
    	}
    	text = StringUtils.trim(text);
    	return Float.parseFloat(text);
    }
    
    /**
     * 字符串转换为int,如果字符串为空则返回默认值
     * @param v
     * @return
     */
    public static int parseIntger(String text, int defval){
    	if(StringUtils.isBlank(text)){
    		return defval;
    	}
    	text = StringUtils.trim(text);
    	return Integer.parseInt(text);
    }
    
    /**
     * 字符串转换为long,如果字符串为空则返回默认值
     * @param v
     * @return
     */
    public static long parseLong(String text, long defval){
    	if(StringUtils.isBlank(text)){
    		return defval;
    	}
    	text = StringUtils.trim(text);
    	return Long.parseLong(text);
    }
    
    /**
     * 格式化数字
     * @param value 数据
     * @param pattern 格式化字符串 输入 "0.00" 格式化为两位小数
     * @return
     */
    public static String formatNumber(Number value, String pattern)
    {
        if(StringUtils.isBlank(pattern)){
            pattern = "0.00";
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        if(value==null){
            return decimalFormat.format(0);
        }
        else{
            return decimalFormat.format(value);
        }
    }
    
    
    public static void main(String[] args){
        System.out.println(Boolean.parseBoolean(null));
        System.out.println(Boolean.parseBoolean("true"));
        System.out.println(Boolean.parseBoolean("truexx"));
    	double a = 11;
    	double b = 3;
    	System.out.println(a/b);
    	System.out.println(div(a,b));
    	System.out.println(round(1.4445445, 3));
    	
    	double t = 123;
    	System.out.println(formatNumber(null, "0.000"));
    	
    	System.out.println((0.5-0.01));
    	System.out.println(sub(0.5,0.01));
    }
}
