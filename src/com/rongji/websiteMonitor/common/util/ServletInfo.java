package com.rongji.websiteMonitor.common.util;

import java.io.File;

import javax.servlet.ServletContext;

/**
 * ServletInfo 提供对单前servlet信息的获取。
 * 
 * @author I-TASK TEAM
 * 
 */
public final class ServletInfo {

	private String serverInfo;

	private int majorVersion;

	private int minorVersion;

	private String servletVersion;

	private String servletRealPath;
	
	private String contextPath;
	
	private ServletContext servletContext;
	
	/**
	 * 通过传递ServletContext 构建信息对象
	 * 
	 * @param context
	 */
	public ServletInfo(ServletContext context) {
		this.servletContext = context;
		this.serverInfo = context.getServerInfo();
		this.majorVersion = context.getMajorVersion();
		this.minorVersion = context.getMinorVersion();
		this.servletVersion = new StringBuffer().append(majorVersion).append('.').append(minorVersion).toString();
		this.servletRealPath = context.getRealPath("/");
		if ((!servletRealPath.endsWith(File.separator))&& (!servletRealPath.endsWith("/"))) {
			servletRealPath += File.separator;
		}
		this.contextPath = context.getContextPath();
	}

	/**
	 * 取得主版本号，如tomcat5.5是2.4的servlet容器，这里将取得2
	 * @return
	 */
	public int getMajorVersion() {
		return majorVersion;
	}

	/**
	 * 取得分版本号，如tomcat5.5是2.4的servlet容器，这里将取得4
	 * @return
	 */
	public int getMinorVersion() {
		return minorVersion;
	}
	/**
	 * 取得服务器描述
	 * @return
	 */
	public String getServerInfo() {
		return serverInfo;
	}
	/**
	 * 取得servlet容器版本号，如tomcat5.5是2.4的servlet容器，这里将取得2.4
	 * @return
	 */
	public String getServletVersion() {
		return servletVersion;
	}

	/**
	 * 取得servlet运行期的真实路径。比如说 E:\tomcat5.5\webapps\myapp\
	 * @return
	 */
	public String getServletRealPath() {
		return servletRealPath;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
	
	/**
	 * 取得应用路径(getContextPath()在servlet 2.5下才有的)。比如说/RJ-CMS
	 * @return
	 */
	public String getContextPath() {
		//是解决相对路径的问题，可返回站点的根路径。 
		return contextPath;
	}

}
