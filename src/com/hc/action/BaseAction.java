package com.hc.action;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hc.sys.R;
import com.hc.sys.RequestProxy;
import com.hc.sys.WebContext;
import com.hc.util.JsonUtil;

/**
 *
 * @author chensh
 */
public abstract class BaseAction {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 取HttpRequest
	 * @return request
	 */
	final protected RequestProxy getReq() {
		return WebContext.get().getRequest();
	}

	/**
	 * 取HttpSession
	 * @return HttpSession
	 */
	final protected HttpSession getSession() {
		return WebContext.get().getSession();
	}

	/**
	 * 取HttpServletResponse
	 * @return HttpServletResponse
	 */
	final protected HttpServletResponse getRes() {
		return WebContext.get().getResponse();
	}

	/**
	 * 取ServletContext
	 * @return
	 */
	final protected ServletContext getServletContext() {
		return WebContext.get().getServletContext();
	}

	/**
	 * forward
	 * @param url forward的相对路径,不包括.jsp
	 * @throws ServletException
	 * @throws IOException
	 */
	final protected void forward(String url) throws ServletException, IOException {
		WebContext.get().forward(url);
	}

	final protected void redirect(String url) throws IOException {
		WebContext.get().redirect(url);
	}

	/**
	 * 向客户端输出字符串，编码为UTF-8
	 * @param text
	 */
	final protected void outputString(String text) {
		logger.debug("输出Json : " + text);
		WebContext.get().outputText(text, WebContext.CONTENTTYPE_HTML, WebContext.CHARTSET_UTF8);
	}

	/**
	 * 向客户端输出处理结果，格式为json，如果request附带callback参赛，在输出jsonp，方法名为callback
	 * @param obj
	 */
	final protected void output(Object obj) {
		// 返回结果
		R rs = null;
		// 如果返回结果是Result
		if (obj instanceof R) {
			rs = (R) obj;
		}
		// 其它类型
		else {
			rs = R.success(obj);
		}

		// 判断输出格式，默认使用JSON
		String callback = getReq().getParameter("callback");

		String jsonString = JsonUtil.toJSONString(rs);
		// JSONP
		if (StringUtils.isNotBlank(callback)) {
			jsonString = callback + "(" + jsonString + ");";
		}
		outputString(jsonString);
	}

	/**
	 * 向客户端输出文件
	 * @param fileName
	 * @param input
	 * @param inline
	 */
	final protected void outputFile(String fileName, InputStream input, boolean inline) {
		try {
			WebContext.get().outputFile(fileName, input, inline);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 从Request获取参数
	 * @param name
	 * @return
	 */
	final protected String p(String name) {
		return this.getReq().getParameter(name);
	}

	/**
	 * 从Request获取参数
	 * @param name
	 * @return
	 */
	final protected String[] ps(String name) {
		return this.getReq().getParameterValues(name);
	}

	/**
	 * 从Request获取参数
	 * @param name
	 * @param defVal
	 * @return
	 */
	final protected String p(String name, String defval) {
		return this.getReq().param(name, defval);
	}

	/**
	 * 从Request获取参数
	 * @param name
	 * @param defval
	 * @return
	 */
	final protected int p(String name, int defval) {
		return this.getReq().param(name, defval);
	}

	/**
	 * 从Request获取参数
	 * @param name
	 * @param defval
	 * @return
	 */
	final protected float p(String name, float defval) {
		return this.getReq().param(name, defval);
	}

	/**
	 * 从Request获取参数
	 * @param name
	 * @param defval
	 * @return
	 */
	final protected double p(String name, double defval) {
		return this.getReq().param(name, defval);
	}

	/**
	 * 从Request获取参数
	 * @param name
	 * @param defval
	 * @return
	 */
	final protected boolean p(String name, boolean defval) {
		return this.getReq().param(name, defval);
	}

	/**
	 * 从Request获取参数
	 * @param name
	 * @param defval
	 * @return
	 */
	final protected Date p(String name, Date defval) {
		return this.getReq().param(name, defval);
	}

	/**
	 * 从Request获取文件
	 * @param name
	 * @param defval
	 * @return
	 */
	final protected FileItem f(String name) {
		return this.getReq().getFile(name);
	}

	/**
	 * 从Request获取文件
	 * @param name
	 * @param defval
	 * @return
	 */
	final protected FileItem[] fs(String name) {
		return this.getReq().getFiles(name);
	}

	/**
	 * 把参数转换到一对一的Map返回，主要用于多条件查询
	 * @return
	 */
	final protected Map<String, String> pm() {
		return this.getReq().getParamMap();
	}

	/**
	 * 把参数转换为Form
	 * @param c
	 * @return
	 */
	final protected <T> T form(Class<T> c) {
		return this.getReq().getForm(c);
	}

	/**
	 * 把request的参数转换成Object，要求客户端传递的是json格式{\"name\":\"陈\"}
	 * 如果字符串为空null\'',则返回null
	 * @param name
	 * @param c
	 * @return
	 */
	final protected <T> T json(String name, Class<T> c) {
		String jsonStr = p(name);
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		} else {
			return JsonUtil.toObject(jsonStr, c);
		}
	}

	/**
	 * 把request的参数转换成List<Object>，要求客户端传递的是json格式[{\"name\":\"陈\"}]
	 * 如果字符串为空null\''，则返回Collections.EMPTY_LIST
	 * @param name
	 * @param c
	 * @return
	 */
	final protected <T> List<T> jsons(String name, Class<T> c) {
		String jsonStr = p(name);
		if (StringUtils.isBlank(jsonStr)) {
			return Collections.emptyList();
		} else {
			return JsonUtil.toObjectList(jsonStr, c);
		}
	}

	/**
	 * 将request cookie封装到Map里面
	 * @return
	 */
	final protected Map<String, Cookie> getCookieMap() {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = this.getReq().getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}

	/**
	 * 根据名字获取cookie的值
	 * @param name
	 * @return
	 */
	final protected String getCookie(String name) {
		Map<String, Cookie> cookieMap = getCookieMap();
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			try {
				return URLDecoder.decode(cookie.getValue(), "UTF-8");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	/**
	 * 设置cookie
	 * @param name  cookie名字
	 * @param value cookie值
	 * @param maxAge cookie生命周期 ,以秒为单位,如果为0则表示这个cookie随着浏览器的关闭即消失
	 * maxAge 可以为正数，表示此cookie从创建到过期所能存在的时间，以秒为单位，此cookie会存储到客户端电脑，以cookie文件形式保存，不论关闭浏览器或关闭电脑，直到时间到才会过期。
	 * 可以为负数，表示此cookie只是存储在浏览器内存里，只要关闭浏览器，此cookie就会消失。maxAge默认值为-1
	 * 还可以为0，表示从客户端电脑或浏览器内存中删除此cookie
	 */
	final protected void setCookie(String name, String value, int maxAge) {
		if (value != null) {
			try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (Exception e) {
			}
		}
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if (maxAge > 0) {
			cookie.setMaxAge(maxAge);
		}
		this.getRes().addCookie(cookie);
	}

	/**
	 * 删除cookie
	 * @param name
	 */
	final protected void deleteCookie(String name) {
		setCookie(name, "", 0);
	}

	/**
	 * 删除所有cookie
	 */
	final protected void cleanCookie() {
		Cookie[] cookies = this.getReq().getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				deleteCookie(cookie.getName());
			}
		}
	}
}
