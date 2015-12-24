package com.rongji.websiteMonitor.common.cache;

import java.util.Properties;

/**
 * 
 * @author XFB
 *
 */
public interface CacheProvider {
	
	 /**
     * 创建缓存,使用配置文件中的配置
     * @param <K>
     * @param <V>
     * @param cacheName
     * @return
     */
	public <K,V>Cache<K,V> buildCache(String cacheName,Properties properties);
	
	/**
	 * 初始化操作,初始化配置的缓冲
	 * @param properties
	 */
	public void init(Properties properties);
	
	/**
	 * 销毁操作,清除所有缓存
	 */
	public void destroy();

}
