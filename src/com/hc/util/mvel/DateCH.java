package com.hc.util.mvel;

import org.mvel2.ConversionHandler;

import com.hc.util.DateUtil;



/**
 * MVEL转换日期型
 * 
 * @author chensh
 */
public class DateCH implements ConversionHandler {
	public Object convertFrom(Object in) {
		return DateUtil.parse(String.valueOf(in));
	}

	public boolean canConvertFrom(Class cls) {
		return true;
	}
}
