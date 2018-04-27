package com.hc.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对Request的参数进行验证，主要用于验证客户端的输入项
 * 
 * @author Administrator
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validates {
	V[] value();

	@Target({})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface V {
		// 参数值的来源，暂时只支持PARAM
		public enum Scope {
			PARAM, SESSION, COOKIE, ATTRIBUTE
		};

		// 参数类型
		public enum Type {
			TEXT, INT, FLOAT, DATE, EMAIL, IP,MOBILEPHONE,TELEPHONE,IDCARD,POSTALCODE
		};

		// 参数值的来源
		Scope scope() default Scope.PARAM;

		// 参数字段名，比如userName
		String fieldName();

		// 参数字段描述，比如用户名
		String fieldDesc();

		// 类型：文本、数值、日期
		Type type() default Type.TEXT;

		// 是否必填
		boolean required() default false;

		// 最小长度，针对文本型
		int minLength() default 0;

		// 最大长度，针对文本型
		int maxLength() default Integer.MAX_VALUE;

		// 最小值，针对数值
		double minValue() default Double.MIN_VALUE;

		// 最大值，针对数值
		double maxValue() default Double.MAX_VALUE;
	}
}
