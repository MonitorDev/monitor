package com.rongji.websiteMonitor.common.login;

import java.util.HashMap;
import java.util.Map;
/**
 * 当前线程上下文变量,用于保存当前登录用户的id及ip地址信息
 * @author xfb
 *
 */
public class ThreadContext {
	
	private static ThreadLocal<Map<String,String>> threadLocal = new ThreadLocal<Map<String,String>>();
	
	public static void setUserId(String userId) {
		getMap().put("userId", userId);
	}
	
	public static String getUserId() {
		return getMap().get("userId");
	}
	
	public static void setUserLoginIp(String loginIp) {
		getMap().put("userLoginIp", loginIp);
	}
	
	public static String getUserLoginIp() {
		return getMap().get("userLoginIp");
	}
	
	public static void clear() {
		threadLocal.remove();
	}

	private static Map<String,String> getMap() {
		Map<String,String> m = threadLocal.get();
		if(m == null) {
			m = new HashMap<String,String>();
			threadLocal.set(m);
		}
		return m;
	}
}
