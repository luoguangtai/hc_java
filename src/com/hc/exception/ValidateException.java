package com.hc.exception;

import java.util.Map;

/**
 * 数据验证异常
 * @author Administrator
 *
 */
public class ValidateException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	//错误信息
	private Map<String, String> errors;

	public ValidateException(Map<String, String> errors){
		this.errors = errors;
	}
	
	public Map<String, String> getErrors(){
		return this.errors;
	}
}
