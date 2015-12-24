package com.rongji.websiteMonitor.common.cache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 缓存工厂,创建管理缓存
 * @author XFB
 *
 */
public class CacheFactory {
	
	private CacheProvider provider;
	
	private Properties properties = null;
	
	private static CacheFactory instance;
	
	private static final Log log = LogFactory.getLog(CacheFactory.class);
	
	
	/**
	 * 
	 */
	private CacheFactory() {
		InputStream in = this.getClass().getResourceAsStream("/cache.properties");
		properties =  new Properties();
		System.out.println(this.getClass().getClassLoader());
		System.out.println(this.getClass().getClassLoader().getClass().getClassLoader());
		URL u = this.getClass().getClassLoader().getResource("");
		URL url = this.getClass().getClassLoader().getResource("/");
		if(in == null) {
			log.warn("Can't find cache.properties in classpath");
		}else {
			try {
				properties.load(in);
			} catch (IOException e) {
				log.error(e);
			}
		}
		String clsName = properties.getProperty("com.rongji.cache.provider");
		try {
		   Class cls = Class.forName(clsName);
		   provider= (CacheProvider) cls.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 单例工厂
	 * @return
	 */
	public static CacheFactory getInstance() {
		if(instance == null) {
			synchronized(CacheFactory.class) {
				if(instance == null) {
					instance = new CacheFactory();
				}
			}
		}
		return instance;
	}

    /**
     * 创建缓存,使用配置文件中的配置
     * @param <K>
     * @param <V>
     * @param cacheName
     * @return
     */
	public  <K,V>Cache<K,V> buildCache(String cacheName) {
		return provider.buildCache(cacheName,this.properties);
	}
	
	public static void main(String[] args) {
		CacheFactory.getInstance();
	}
	
}
