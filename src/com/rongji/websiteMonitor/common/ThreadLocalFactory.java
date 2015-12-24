package com.rongji.websiteMonitor.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.framework.FrameworkConstants;
import com.rongji.websiteMonitor.common.util.Utils;


/**
 * ThreadLocal工厂 4 CMS
 * @author jayfans
 * @version 1.0
 * @since 2013-01-06 
 */
public class ThreadLocalFactory {
	
	//线程局部变量
	private static ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();
	
	//Request key 
	private static final String KEY_REQUEST="request-key";
	
	//singleThreadRunnerCache key 
	private static final String KEY_SINGLE_THREAD_CACHE="singleThreadRunnerCache-key";
	
	//rjspider-user-key
	public static final String KEY_NO_LOGIN_USERID="no-login-userId-key";
	
	/**
	 * 获取本地线程变量的request
	 * @return
	 */
	public static HttpServletRequest getThreadLocalRequest(){ 
		return (HttpServletRequest)get(KEY_REQUEST); 
	}
	
	/**
	 * 设置本地线程变量的request
	 * @return
	 */
    public static void setThreadLocalRequest(HttpServletRequest request){ 
    	put(KEY_REQUEST,request); 
    }
    
    /**
     * 获取本地线程局部变量
     * @param key
     * @return
     */
    public static Object get(String key){
    	Map map = (Map)threadLocal.get();
    	if(map==null){
    		return null;
    	}
    	return map.get(key);
    }
    
    /**
     * 添加本地线程局部变量
     * @param key
     * @return oldValue
     */
    public static Object put(String key,Object value){
		Map map = (Map)threadLocal.get();
		if(map==null){
			map = new HashMap();
		}
		Object put = map.put(key, value);
		threadLocal.set(map);
		return put;
    }
    
    /**
     * 清空线程变量
     */
    public static void clear(){
    	Object object = threadLocal.get();
    	object = null;
    	threadLocal.set(null); 
    }
    
    /**
     * 获取当前线程用户ID
     * @return
     */
    public static String getCurrentUserId()
    {
    	HttpServletRequest threadLocalRequest = getThreadLocalRequest();
    	String userId = null;
    	//无需登录时，设置的USERID
    	if(ThreadLocalFactory.get(KEY_NO_LOGIN_USERID)!=null){
    		userId = (String)ThreadLocalFactory.get(KEY_NO_LOGIN_USERID);
    	}
    	if(Utils.isEmpty(userId)&&threadLocalRequest!=null){
    		userId = (String)threadLocalRequest.getSession().getAttribute(FrameworkConstants.LOGIN_USER_KEY);
    	}
    	return userId;
    }
    
    /**
     * <h3>注册当前线程的单执行线程对象</h3>
     *    <p>该单执行线程对象将以单线程的方式执行添加的任务队列</p>
     * @param registerName
     * @return
     */
    public static SingleThreadRunner registerSingleThreadRunner(String registerName){
    	if(Utils.isEmpty(registerName)){
    		return null;
    	}
    	Map<String, SingleThreadRunner> singleThreadRunnerCache = getCurrRegisterSingleThreadRunnerCache();
    	SingleThreadRunner singleThreadRunner = singleThreadRunnerCache.get(registerName);
    	if(singleThreadRunner == null){
    		singleThreadRunner = new SingleThreadRunner();
    		singleThreadRunnerCache.put(registerName, singleThreadRunner);
    	}
    	return singleThreadRunner;
    }
    
    /**
    * <h3>获取已经注册到当前线程的单执行线程对象</h3>
     *    <p>该单执行线程对象将以单线程的方式执行添加的任务队列</p>
     * @param registerName
     * @return
     */
    public static SingleThreadRunner getRegSingleThreadRunner(String registerName){
    	return getCurrRegisterSingleThreadRunnerCache().get(registerName);
    }
    
    /**
     * 获取注删除到当前线程的SingleThreadRunner对象集合
     * @return Map<String,SingleThreadRunner>
     */
    private static Map<String,SingleThreadRunner> getCurrRegisterSingleThreadRunnerCache(){
    	Map<String,SingleThreadRunner> runnerCache = (Map<String,SingleThreadRunner>)get(KEY_SINGLE_THREAD_CACHE);
    	if(runnerCache==null){
    		runnerCache = new HashMap<String, SingleThreadRunner>();
    		put(KEY_SINGLE_THREAD_CACHE, runnerCache);
    	}
    	return runnerCache;
    }
    
}
