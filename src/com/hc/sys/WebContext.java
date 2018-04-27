package com.hc.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.hc.util.MIMEUtil;

/**
 * Servlet相关资源
 * @author 陈顺华
 */
public class WebContext {
	private static final ThreadLocal<WebContext> _contexts = new ThreadLocal<WebContext>();
	private RequestProxy request;
	private HttpServletResponse response;
	private ServletContext servletContext;

	public static final String CONTENTTYPE_HTML = "text/html";
	public static final String CHARTSET_UTF8 = "utf-8";
	private static final String JSP = ".jsp";

	protected static void begin(HttpServletRequest req, HttpServletResponse res, ServletContext servletcontext) {
		WebContext wc = new WebContext(req, res, servletcontext);
		_contexts.set(wc);
	}

	/**
	 * 从class取web应用路径，结尾带/
	 * @return
	 */
	public static String getWebContentPath() {
		String str = WebContext.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		try {
			str = URLDecoder.decode(str, "UTF-8");// //转换处理中文及空格
		} catch (UnsupportedEncodingException e) {
		}
		// 查找“WEB-INF”在该字符串的位置 ，截取
		if (str.indexOf("WEB-INF") > -1) {
			str = str.substring(0, str.indexOf("WEB-INF"));
		}
		return new File(str).getAbsolutePath() + File.separator;
	}

	public static void main(String[] args) {
		System.out.println(getWebContentPath());
	}

	protected static void end() {
		WebContext wc = WebContext.get();
		if (wc != null) {
			wc.request = null;
			wc.response = null;
			wc.servletContext = null;
			_contexts.remove();
		}
	}

	public static WebContext get() {
		return _contexts.get();
	}

	private WebContext(HttpServletRequest req, HttpServletResponse res, ServletContext servletContext) {
		try {
			this.request = new RequestProxy((HttpServletRequest) req, Config.getFileUploadMaxSize());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		this.response = res;
		this.servletContext = servletContext;
	}

	public RequestProxy getRequest() {
		return request;
	}

	public HttpSession getSession() {
		return request.getSession(true);
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * 取web目录的绝对路径,以文件分隔符结尾
	 * @return
	 */
	public String getAppPath() {
		String path = servletContext.getRealPath("/");
		// 在部分服务器下，比如websphere、weblogic下面不带分隔符
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		return path;
	}

	/**
	 * 根据相对路径取文件
	 * @param path
	 * @return
	 */
	public File getFile(String path) {
		return new File(servletContext.getRealPath(path));
	}

	public void forward(String url) throws ServletException, IOException {
		request.getRequestDispatcher(Config.getPagePath() + url + JSP).forward(request, response);
	}

	public void redirect(String url) throws IOException {
		response.sendRedirect(request.getContextPath() + Config.getPagePath() + url + JSP);
	}

	/**
	 * 判断浏览器是否支持 gzip 压缩  ，如果服务器配置不使用gzip，或者客户端指定gzip=0则也不使用gzip压缩
	 * @return
	 */
	private boolean isGzipSupport() {
		String headEncoding = request.getHeader("accept-encoding");
		// 客户端 不支持 gzip,服务器配置不使用gzip，客户端要求不使用gzip
		if (headEncoding == null || (headEncoding.indexOf("gzip") == -1) || !Config.isGzipEnabled() || "0".equals(request.getParameter("gzip"))) {
			return false;
		}
		// 支持 gzip 压缩
		else {
			return true;
		}
	}

	/**
	 * 向HttpServletResponse输出文本
	 * @param text 输出的字符串
	 * @param contentType 类型
	 * @param charset 编码
	 */
	public void outputText(String text, String contentType, String charset) {
		response.setCharacterEncoding(charset);
		// 指定内容类型
		response.setContentType(contentType + ";charset=" + charset);
		// 禁止缓存
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			// 支持gzip，且长度大于指定阀值的时候
			byte[] content = text.getBytes(charset);
			if (isGzipSupport() && content.length >= Config.getGzipMinSize()) {
				response.setHeader("content-encoding", "gzip");
				OutputStream o = response.getOutputStream();
				GZIPOutputStream gz = new GZIPOutputStream(o);
				gz.write(content);
				gz.finish();
				gz.close();
				o.close();
			} else {
				OutputStream o = response.getOutputStream();
				o.write(content);
				o.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向HttpServletResponse输出文件，此方法完成后并不关闭InputStream，需要自行在外面通过IOUtils.closeQuietly关闭
	 * @param fileName
	 * @param input
	 * @param inline,直接打开还是下载，如果为true则直接打开，否则下载
	 */
	public void outputFile(String fileName, InputStream input, boolean inline) throws IOException {
		String filetype = FilenameUtils.getExtension(fileName);
		String name = URLEncoder.encode(fileName, "UTF-8");

		OutputStream output = response.getOutputStream();
		// inline--直接打开 attachment--下载
		response.setHeader("Content-Disposition", inline ? "inline" : "attachment" + "; filename=" + name);// 下载
		response.setContentType(MIMEUtil.getMIMEType(filetype));
		// 如果是文件流，支持断点续传
		if (input instanceof FileInputStream) {
			// 得到文件大小，不能用input.available()
			long fSize = ((FileInputStream) input).getChannel().size();
			// 下载起始位置
			long since = 0;
			// 下载结束位置
			long until = fSize - 1;
			// 获取Range，下载范围
			String range = request.getHeader("range");
			if (range != null) {
				// 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
				// 剖解range
				range = range.split("=")[1];
				String[] rs = range.split("-");
				if (StringUtils.isNumeric(rs[0])) {
					since = Long.parseLong(rs[0]);
				}
				if (rs.length > 1 && StringUtils.isNumeric(rs[1])) {
					until = Long.parseLong(rs[1]);
				}
			}

			// 设置响应头
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Content-Range", "bytes " + since + "-" + until + "/" + fSize);
			response.setHeader("Content-Length", (until - since + 1) + "");
			IOUtils.copyLarge(input, output, since, until - since + 1);
		} else {
			IOUtils.copy(input, output);
		}
		// 输出
		output.flush();
		output.close();
	}
}
