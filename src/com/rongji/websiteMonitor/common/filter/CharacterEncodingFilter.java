package com.rongji.websiteMonitor.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.rongji.websiteMonitor.common.ThreadLocalFactory;




/**
 * 字符数据过滤(用户解决字符编码问题)
 * 
 * @author  RJ-CMS7 TEAM
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public final class CharacterEncodingFilter implements Filter {
	protected String encoding = null;
	protected FilterConfig filterConfig = null;
	protected boolean ignore = true;
	
	/**
	 * 默认构造函数
	 */
	public CharacterEncodingFilter() {
	}

	/**
	 * 进行字符编码转换过滤
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * 
	 * @throws java.io.IOException
	 * @throws javax.servlet.ServletException
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		if ((this.encoding != null) && (this.ignore || request.getCharacterEncoding() == null)) {
			request.setCharacterEncoding(this.encoding);
		}
		//设置threadLocal变量
		ThreadLocalFactory.setThreadLocalRequest((HttpServletRequest)request);
		// 把处理权发送到下一个 .也就是加入过滤链.
		try {
			filterChain.doFilter(request, response);
		}finally{
			ThreadLocalFactory.clear();
		}
	}

	/**
	 * 初始化配置
	 * 
	 * @param filterConfig
	 * @throws ServletException
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
		String str = filterConfig.getInitParameter("ignore");
		if (str == null)
			this.ignore = true;
		else if (str.equalsIgnoreCase("true"))
			this.ignore = true;
		else if (str.equalsIgnoreCase("yes"))
			this.ignore = true;
		else
			this.ignore = false;
	}

	/**
	 * 销毁过滤器
	 */
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	/**
	 * 设置编码
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 获取配置
	 * 
	 * @return FilterConfig
	 */
	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	/**
	 * 设置配置
	 * 
	 * @param filterConfig
	 */
	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}
}
