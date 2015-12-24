package com.rongji.websiteMonitor.common.cache.ehcache;

import java.util.Properties;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rongji.websiteMonitor.common.cache.Cache;
import com.rongji.websiteMonitor.common.cache.CacheProvider;

public class EhCacheProvider implements CacheProvider {

	private static final Log log = LogFactory.getLog(EhCacheProvider.class);

	private CacheManager manager;
	
	public EhCacheProvider(){
		this.init(null);
	}

	@Override
	public  Cache buildCache(
			String cacheName,Properties properties) {
		try {
			net.sf.ehcache.Cache cache = manager.getCache(cacheName);
			if (cache == null) {
				log.warn("Could not find configuration [" + cacheName
						+ "]; using defaults.");
				manager.addCache(cacheName);
				cache = manager.getCache(cacheName);
				log.debug("started EHCache region: " + cacheName);
			}
			return new EhCache(cache, cacheName);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
	}

	@Override
	public void destroy() {
		if (manager != null) {
			manager.shutdown();
			manager = null;
		}

	}

	@Override
	public void init(Properties properties) {
		if (manager != null) {
			log
					.warn("Attempt to restart an already started EhCacheProvider. Use sessionFactory.close() "
							+ " between repeated calls to buildSessionFactory. Using previously created EhCacheProvider."
							+ " If this behaviour is required, consider using net.sf.ehcache.hibernate.SingletonEhCacheProvider.");
			return;
		}
		try {
			manager = new CacheManager();
		} catch (net.sf.ehcache.CacheException e) {
			throw e;
		}

	}

}
