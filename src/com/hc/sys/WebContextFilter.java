package com.hc.sys;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author chensh
 */
public class WebContextFilter implements Filter {
	private ServletContext context;

	public void init(FilterConfig filterConfig) {
		context = filterConfig.getServletContext();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 把req、res和servletcontext存入WebContext
		WebContext.begin((HttpServletRequest) request,
				(HttpServletResponse) response, context);
		try {
			chain.doFilter(request, response);
		} finally {
			WebContext.end();
		}
	}

	public void destroy() {
	}
}
