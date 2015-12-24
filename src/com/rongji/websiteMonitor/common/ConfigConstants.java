package com.rongji.websiteMonitor.common;



import java.io.File;

import org.apache.log4j.Logger;


import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.util.Utils;


/**
 * 系统属性配置常量定义
 * 
 * <p>Title: 榕基RJ-CMSV7.X</p>
 * <p>Description:用于取得系统属性配置的常量</p> </p>
 * <p>Copyright: Copyright (c) 2009-2011</p>
 * <p>Company: 榕基软件开发有限公司</p>
 * 
 * @author HQJ
 * @version 1.0
 * @since	1.0.0	HQJ		2009-12-01
 */
public class ConfigConstants {
	public static ConfigConstants instance = null;
	/** 定义本系统基本参数配置文件名 */
	private static final String CMS_CONFIG_FILE = "website_config.xml";
	/** 系统路径分隔符 "\" */
	public static final String FILE_SPT = File.separator;
	/** 路径分隔符 "/" */
	public static final char SPT = '/';
	/** WEB-INF */
	public static final String WEB_INF = "WEB-INF";
	
	

	/**
	 * 取得默认构造函数的实例
	 * 
	 * @return
	 */
	public static ConfigConstants getInstance() {
		if (instance == null) {
			synchronized (ConfigConstants.class) {
				if (instance == null) {
					instance = new ConfigConstants();
				}
			}
		}
		return instance;
	}

	// ----------------------------------------------------------公用获取系统配置参数
	/**
	 * 取得class的path路径
	 * 
	 * @return
	 */
	public String getClassesPath() {
		String path = this.getClass().getResource(this.getClass().getName()).getPath();
		int index = path.indexOf("WEB-INF");
		if (index < 0) {
			path = path.substring(0, path.indexOf("/com/") + 1);
		} else {
			path = path.substring(0, index) + "WEB-INF/classes/";
		}
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
			if (path.startsWith("/")) {
				path = path.substring(1);
			} else if (path.startsWith("file:/")) {
				path = path.substring(6);
			}
		} else if (path.startsWith("file:/")) {
			path = path.substring(5);
		}
		path = path.replaceAll("%20", " ");
		return path;
	}

	
	/**
	 * 取得系统调试配置信息
	 * 
	 * @return
	 */
	public boolean isDebug() {
		
		boolean isDebug = "true".equals(FrameworkHelper.getSystemConfig("website.sysbasearg.debugMode", "false"));
		
		return isDebug;
	}

	/**
	 * 获取邮件发送的发送主邮件
	 * @return
	 */
	public String getEmail() {
		
		String email = FrameworkHelper.getSystemConfig("website.sysbasearg.email", "");
		
		return email;
	}
	
	/**
	 * 获取即时通短信的发送的URL
	 * @return
	 */
	public String getSendUrl(){
		return FrameworkHelper.getSystemConfig("website.sendarg.sendUrl", ""); 
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLogUrl(){
		return FrameworkHelper.getSystemConfig("website.sendarg.logUrl", "");
	}
	
	/**
	 * 获取即时通的账户
	 * @return
	 */
	public String getSendArgUserName(){
		return FrameworkHelper.getSystemConfig("website.sendarg.userName", "");
	}
	
	/**
	 * 获取即时通的账户密码
	 * @return
	 */
	public String getSendArgPwd(){
		return FrameworkHelper.getSystemConfig("website.sendarg.pwd", "");
	}
	
	/**
	 * 获取短信发送的字符集编码
	 * @return
	 */
	public String getsengArgCharset(){
		return FrameworkHelper.getSystemConfig("website.sendarg.charset", "GBK");
	}
	
	private static final Logger logger = Logger.getLogger(ConfigConstants.class);

	

}
