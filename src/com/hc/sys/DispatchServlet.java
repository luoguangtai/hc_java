package com.hc.sys;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hc.action.BaseAction;
import com.hc.exception.AppException;
import com.hc.exception.ValidateException;
import com.hc.exception.ValidateExceptionUtil;
import com.hc.util.JsonUtil;

/**
 * 控制器，负责转码、验证登陆和分发，根据action调用相应的Service
 * 
 * @author 
 * 
 */
public class DispatchServlet extends HttpServlet {
	private static final long serialVersionUID = -7384522681268869771L;
	// LOG
	private final Logger logger = LoggerFactory.getLogger(getClass());
	// 分隔符
	private static final String SEPARATOR = "!";
	// 存放action的Map
	private static final Map<String, BaseAction> actionMap = new HashMap<String, BaseAction>();
	// 存放method的Map
	private static final Map<String, Method> methodMap = new HashMap<String, Method>();

	/**
	 * @param config config 
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// action
		String actionNames[] = SpringUtil.getBeanNamesForType(BaseAction.class);
		for (int i = 0; i < actionNames.length; i++) {
			String actionName = actionNames[i];
			BaseAction action = (BaseAction) SpringUtil.getBean(actionNames[i]);
			actionMap.put(actionName, action);

			// method
			Method[] methods = action.getClass().getMethods();
			for (int j = 0; j < methods.length; j++) {
				Method method = methods[j];
				// 只加载public void，且没有参数的方法；另外如果用了拦截器，由于动态代理的原因会导致getModifiers=17
				if ((Modifier.isPublic(method.getModifiers()) || method.getModifiers() == 17) && method.getParameterTypes().length == 0
						&& method.getReturnType().equals(Void.TYPE)) {
					methodMap.put(actionName + SEPARATOR + method.getName(), method);
				}
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestProxy req = WebContext.get().getRequest();
		// action
		String uri = req.getRequestURI();
		String action = "page";
		if (uri.indexOf(".do") > -1) {
			action = uri.substring(uri.lastIndexOf("/") + 1, uri.indexOf("."));
		}

		// 当请求为page.do?url=****的时候转向到/WEB-INF/jsp目录下的jsp文件
		if ("page".equals(action)) {
			String url = req.getParameter("url");
			if (StringUtils.isBlank(url)) {
				url = "flexIndex";
			}
			WebContext.get().forward(url);
			return;
		}

		logger.debug("***************【" + action + ", from " + req.getRemoteAddr() + "】 请求开始***************");
		// 记录客户端传过来的参数
		Map<String, String[]> params = req.getParameterMap();
		StringBuilder sb;
		for (String key : params.keySet()) {
			sb = new StringBuilder("参数 : ").append(key).append("=");
			if (params.get(key).length == 1) {
				sb.append(params.get(key)[0]);
			} else {
				sb.append("[");
				for (int i = 0; i < params.get(key).length; i++) {
					if (i > 0) {
						sb.append(",");
					}
					sb.append(params.get(key)[i]);
				}
				sb.append("]");
			}
			logger.debug(sb.toString());
		}
		try {
			// 执行Action
			process(action, req);
		} catch (Exception e) {
			e.printStackTrace();
			// 创建错误消息
			R rs = R.failure(e.getMessage());
			if (e instanceof ValidateException) {
				rs = R.failure(((ValidateException) e).getErrors());
			}
			WebContext.get().outputText(JsonUtil.toJSONString(rs), WebContext.CONTENTTYPE_HTML, WebContext.CHARTSET_UTF8);
		}
	}

	/**
	 * 响应请求,根据action判断模块，交由具体的模块类进行处理
	 * @param action action!method
	 * @return
	 */
	private void process(String action, RequestProxy req) throws ValidateException, AppException {
		// action为空
		if (StringUtils.isBlank(action)) {
			throw new AppException("action不能为空");
		}
		// 把action拆分成beanName和methodName
		String[] s = action.split(SEPARATOR);
		// action的格式必须是 a!b
		if (s.length != 2 || StringUtils.isBlank(s[0]) || StringUtils.isBlank(s[1])) {
			throw new AppException("action格式错误:" + action + ",正确格式应该是actionName!methodName");
		}

		BaseAction bean = actionMap.get(s[0]);
		Method method = methodMap.get(action);

		if (bean == null) {
			throw new AppException("没有定义ActionBean：" + s[0]);
		}

		if (method == null) {
			throw new AppException(s[0] + "中不存在方法：" + s[1]);
		}

		// 对输入参数做验证，如果验证结果不为空则表示有输入项不符合要求，抛出ValidateException
		Map<String, String> validateErrors = ValidateExceptionUtil.validateRequest(method, req);
		if (!validateErrors.isEmpty()) {
			throw new ValidateException(validateErrors);
		}
		// 验证通过，调用相应的Action
		try {
			method.invoke(bean);
		} catch (AppException e) {
			throw e;
		} catch (SecurityException e) {
			throw new AppException("SecurityException：" + action);
		} catch (InvocationTargetException e) {
			throw new AppException(e.getTargetException());
		} catch (Exception e) {
			throw new AppException(e);
		}
	}
}
