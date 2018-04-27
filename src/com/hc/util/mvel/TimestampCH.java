package com.hc.util.mvel;

import org.mvel2.ConversionHandler;

import com.hc.util.DateUtil;



/**
 * MVEL转换Timestamp
 * @author chensh
 */
public class TimestampCH implements ConversionHandler {
    public Object convertFrom(Object in) {
        return DateUtil.parse2Timestamp(String.valueOf(in));
    }

    public boolean canConvertFrom(Class cls) {
        return true;
    }
}
