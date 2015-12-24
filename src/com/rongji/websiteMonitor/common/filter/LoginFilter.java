package com.rongji.websiteMonitor.common.filter;

import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;


import java.io.IOException;
import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.websiteMonitor.common.Constants;



/**
 * 用于登录校验过滤
 * 
 * @author  RJ-CMS7 TEAM
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class LoginFilter implements Filter {
	private FilterConfig filterConfig = null;
	private boolean checkLogin;

	/**
	 * 默认构造函数
	 */
	public LoginFilter() {
		this.checkLogin = true;
	}
	

	/**
	 * 过滤的内容包括
	 * 1 是否登录
	 * 2 登录的人员是否拥有当前URL的访问权限。 
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)servletRequest;
		HttpServletResponse response =(HttpServletResponse)servletResponse;
		if (!this.checkLogin){
			chain.doFilter(request, response);
			return;
		}
		String loginUser="";//(String) request.getSession().getAttribute(Constants.LOGIN_UMS_USER_KEY);
		if(loginUser != null){
			chain.doFilter(request, response);
			return;
		}

		String[] URI_WITHOUT_LOGIN={"login.sp"};//FIXME
		for(String uri:URI_WITHOUT_LOGIN){
			if(request.getRequestURI()!=null&&request.getRequestURI().endsWith(uri)){
				chain.doFilter(request, response);
				return;
			}
		}
		if(request.getRequestURI()!=null&&request.getRequestURI().endsWith("index.sp") && 
				"index".equals(request.getParameter("act"))){
			request.getRequestDispatcher("login.sp?act=index").forward(request, response);
			return;
		}
//		chain.doFilter(request, response);//FIXME
//		response.sendError(HttpServletResponse.SC_FORBIDDEN);
//		outPutXML(response, cg);
//		LoginController c=new LoginController();
//		try {
//			c.index(request, response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String act=request.getParameter("act");
		JSCommand cmd=new JSCommand(null,"window.location.replace(\"./index.jsp\")");
		outPutXML(response, cmd);
		//window.location.replace(\"./index.jsp\")
		return;
	}
	/**
	 * 初始化配置
	 * 
	 * @param filterConfig
	 * @throws ServletException
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig=filterConfig;
		String checkLogin=filterConfig.getInitParameter("checkCode");
		if (checkLogin ==null){
			this.checkLogin=true;
		}else if (checkLogin.equalsIgnoreCase("false")){
			this.checkLogin=false;
		}else{
			this.checkLogin=true;
		}
	}
	
	/**
	 * 销毁过滤器 
	 */
	public void destroy() {
		 this.filterConfig = null;
		
	}
	/**
	 * 设置配置
	 * @param filterConfig FilterConfig
	 */
	public void setFilterConfig(FilterConfig filterConfig) {
	   this.filterConfig = filterConfig;
	}

	/**
	 * 设置是否需要登录
	 * 
	 * @param forceEncoding
	 */
	public void setCheckLogin(boolean checkLogin) {
		this.checkLogin = checkLogin;
	}

}
