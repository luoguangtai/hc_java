package com.hc.exception;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.hc.sys.RequestProxy;
import com.hc.util.DateUtil;
import com.hc.exception.Validates.V;
import com.hc.exception.Validates.V.Scope;
import com.hc.exception.Validates.V.Type;

public class ValidateExceptionUtil {
	private static final String MESSAGE_NOTALLOW_BLANK = "不能为空";
	private static final String MESSAGE_NOTALLOW_LENGTHLESSTHAN = "长度不能小于";
	private static final String MESSAGE_NOTALLOW_LENGTHGREATERTHAN = "长度不能大于";
	private static final String MESSAGE_MUST_ISINT = "必须是整数";
	private static final String MESSAGE_NOTALLOW_VALUELESSTHAN = "不能小于";
	private static final String MESSAGE_NOTALLOW_VALUEGREATERTHAN = "不能大于";
	private static final String MESSAGE_FORMATERROR = "格式不正确";

	/**
	 * 对request进行数据验证，包括param、session、cookie，暂时只支持param
	 * 
	 * @param method
	 * @return
	 */
	public static Map<String, String> validateRequest(Method method,
			RequestProxy req) {
		Validates vs = method.getAnnotation(Validates.class);
		// 如果没有设置注解，返回空Map
		if (vs == null) {
			return Collections.emptyMap();
		}

		// 错误消息
		Map<String, String> errors = new HashMap<String, String>();
		// 客户端get/post的参数
		Map<String, String> params = req.getParamMap();
		// 注解明细
		V[] v = vs.value();
		String text = null;
		String error = null;
		for (int i = 0; i < v.length; i++) {
			text = null;
			error = null;
			// 根据scope判断是从param、cookie、session里取值
			if (v[i].scope() == Scope.PARAM) {
				text = params.get(v[i].fieldName());
			}

			// 根据数据类型type调用验证方法
			if (v[i].type() == Type.TEXT) {
				error = vText(text, v[i].required(), v[i].minLength(),
						v[i].maxLength());
			} else if (v[i].type() == Type.INT) {
				error = vInt(text, v[i].required(), v[i].minValue(),
						v[i].maxValue());
			} else if (v[i].type() == Type.FLOAT) {
				error = vFloat(text, v[i].required(), v[i].minValue(),
						v[i].maxValue());
			} else if (v[i].type() == Type.DATE) {
				error = vDate(text, v[i].required());
			} else if (v[i].type() == Type.EMAIL) {
				error = vEmail(text, v[i].required());
			} else if (v[i].type() == Type.IP) {
				error = vIp(text, v[i].required());
			} else if (v[i].type() == Type.MOBILEPHONE) {
				error = vMobilePhone(text, v[i].required());
			} else if (v[i].type() == Type.TELEPHONE) {
				error = vTelephone(text, v[i].required());
			} else if (v[i].type() == Type.IDCARD) {
				error = vIdCard(text, v[i].required());
			} else if (v[i].type() == Type.POSTALCODE) {
				error = vPostalCode(text, v[i].required());
			}
			// error不为null则表示有错误,加到errors
			if (error != null) {
				errors.put(v[i].fieldName(), v[i].fieldDesc() + error);
			}
		}
		return errors;
	}

	/**
	 * 正则表达式验证方法 匹配表达式则返回true不匹配则返回false
	 * 
	 * @param regex
	 *            存放正则表达式
	 * @param str
	 *            传入待验证参数
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 验证文本字符串，如果返回null则表示通过，否则返回错误消息
	 * 
	 * @param text
	 * @param required
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static String vText(String text, boolean required, int minLength,
			int maxLength) {
		// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		if (StringUtils.isBlank(text)) {
			return required ? MESSAGE_NOTALLOW_BLANK : null;
		} else {
			// 如果有内容，则判断字符串长度是否在要求范围内
			int len = text.length();
			if (len < minLength) {
				return MESSAGE_NOTALLOW_LENGTHLESSTHAN + minLength;
			}
			if (len > maxLength) {
				return MESSAGE_NOTALLOW_LENGTHGREATERTHAN + maxLength;
			}
			return null;
		}
	}

	/**
	 * 验证整数，如果返回null则表示通过，否则返回错误消息
	 * 
	 * @param text
	 * @param required
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static String vInt(String text, boolean required, double minValue,
			double maxValue) {
		// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		if (StringUtils.isBlank(text)) {
			return required ? MESSAGE_NOTALLOW_BLANK : null;
		} else {
			// 如果有内容，则必须是数字
			if (!NumberUtils.isDigits(text)) {
				return MESSAGE_MUST_ISINT;
			}
			// 再验证数值范围
			int v = NumberUtils.toInt(text);
			if (v < minValue) {
				return MESSAGE_NOTALLOW_VALUELESSTHAN + minValue;
			}
			if (v > maxValue) {
				return MESSAGE_NOTALLOW_VALUEGREATERTHAN + maxValue;
			}
			return null;
		}
	}

	/**
	 * 验证小数，如果返回null则表示通过，否则返回错误消息
	 * 
	 * @param text
	 * @param required
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static String vFloat(String text, boolean required, double minValue,
			double maxValue) {
		// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		if (StringUtils.isBlank(text)) {
			return required ? MESSAGE_NOTALLOW_BLANK : null;
		} else {
			// 如果有内容，则必须是数字
			if (!NumberUtils.isNumber(text)) {
				return MESSAGE_MUST_ISINT;
			}
			// 再验证数值范围
			int v = NumberUtils.toInt(text);
			if (v < minValue) {
				return MESSAGE_NOTALLOW_VALUELESSTHAN + minValue;
			}
			if (v > maxValue) {
				return MESSAGE_NOTALLOW_VALUEGREATERTHAN + maxValue;
			}
			return null;
		}
	}

	/**
	 * 验证日期,如果返回null则表示通过，否则返回错误消息
	 * 
	 * @param text
	 * @param required
	 * @return
	 */
	public static String vDate(String text, boolean required) {
		// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		if (StringUtils.isBlank(text)) {
			return required ? MESSAGE_NOTALLOW_BLANK : null;
		} else {
			// 判断是不是日期格式
			if (DateUtil.isDate(text)) {
				return MESSAGE_FORMATERROR;
			}
			return null;
		}
	}

	/**
	 * 验证Email格式,如果返回null则表示通过，否则返回错误消息
	 * 
	 * @param text
	 * @param required
	 * @return
	 */
	public static String vEmail(String text, boolean required) {
		// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		if (StringUtils.isBlank(text)) {
			return required ? MESSAGE_NOTALLOW_BLANK : null;
		} else {
			// 判断是不是Email格式
			// 存放正则表达式
			String regex = "";
			regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
			if (!match(regex, text)) {
				return MESSAGE_FORMATERROR + "Email格式为：用户名@服务器地址";
			}
		}
		return null;
	}

	/**
	 * 验证IP地址格式,如果返回null则表示通过，否则返回错误消息
	 * 
	 * @param text
	 * @param required
	 * @return
	 */
	public static String vIp(String text, boolean required) {
		// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		if (StringUtils.isBlank(text)) {
			return required ? MESSAGE_NOTALLOW_BLANK : null;
		} else {
			// 判断是不是Email格式
			// 存放正则表达式
			String regex = "";
			String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
			regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
			if (!match(regex, text)) {
				return MESSAGE_FORMATERROR + "IP地址格式为：四段，三个圆点分割，每段取值从0-255";
			}
		}
		return null;
	}

	/**
	 * 验证是否是手机号(规则：以1开头，接任意数字，11位),如果返回null则表示通过，否则返回错误消息
	 * 
	 * @param text
	 * @param required
	 * @return
	 */
	public static String vMobilePhone(String text, boolean required) {
		// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		if (StringUtils.isBlank(text)) {
			return required ? MESSAGE_NOTALLOW_BLANK : null;
		} else {
			// 判断是不是手机号码格式
			// 存放正则表达式
			String regex = "";
			regex = "^[1]+\\d{10}";
			if (!match(regex, text)) {
				return MESSAGE_FORMATERROR + "手机号码规则为：以1开头，接任意数字，总共11位";
			}
		}
		return null;

	}

	/**
	 * 验证是否是固定电话(规则：xxxx(区号，3或4位)-(连接线，必备)xxxxxx(电话，6到8位),如028-83035137)
	 * 
	 * @param text
	 *            参数
	 * @param required
	 *            是否必填
	 * @return
	 */
	public static String vTelephone(String text, boolean required) {
		// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		if (StringUtils.isBlank(text)) {
			return required ? MESSAGE_NOTALLOW_BLANK : null;
		} else {
			// 判断是不是电话号码格式
			// 存放正则表达式
			String regex = "";
			regex = "^(\\d{3,4}-)?\\d{6,8}$";
			if (!match(regex, text)) {
				return MESSAGE_FORMATERROR
						+ "固定电话格式为：xxxx(区号，3或4位)-xxxxxx(电话，6到8位)如：如0871-63128243";
			}
		}
		return null;
	}

	/**
	 * 验证15位或18位身份证号
	 * 
	 * @param text
	 * @param required
	 * @return
	 */
	public static String vIdCard(String text, boolean required) {
		// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		if (StringUtils.isBlank(text)) {
			return required ? MESSAGE_NOTALLOW_BLANK : null;
		} else {
			// 判断是不是身份证号码格式
			// 存放正则表达式
			String regex = "";
			regex = "(^\\d{18}$)|(^\\d{15}$)";
			if (!match(regex, text)) {
				return MESSAGE_FORMATERROR + "身份证号码格式为：15位或18位整数";
			}
		}
		return null;
	}

	/**
	 * 验证是否为邮编地址
	 * 
	 * @param text
	 * @param required
	 * @return
	 */
	public static String vPostalCode(String text, boolean required) {// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		// 如果文本内容为空，则看是不是必填项，如果是必填则返回错误消息，否则验证通过
		if (StringUtils.isBlank(text)) {
			return required ? MESSAGE_NOTALLOW_BLANK : null;
		} else {
			// 判断是不是邮编格式
			// 存放正则表达式
			String regex = "";
			regex = "^\\d{6}$";
			if (!match(regex, text)) {
				return MESSAGE_FORMATERROR + "邮编格式为:6位正整数";
			}
		}
		return null;
	}

}
