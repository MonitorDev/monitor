package com.rongji.websiteMonitor.common.cache.ehcache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rongji.websiteMonitor.common.cache.Cache;



public class EhCache<K,V> implements Cache<K,V> {

	 private static final Log log = LogFactory.getLog(EhCache.class);

	/**
	 * 
	 */
	private net.sf.ehcache.Ehcache cache = null;
	/**
	 * 
	 */
	private String cacheName = null;

	public EhCache(net.sf.ehcache.Ehcache cache, String cacheName) {
		this.cache = cache;
		this.cacheName = cacheName;
	}

	@Override
	public boolean clear() {
		try {
			cache.removeAll();
		} catch (IllegalStateException e) {
			throw new CacheException(e);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
		return true;
	}

	@Override
	public V get(Object key) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("key: " + key);
			}
			if (key == null) {
				return null;
			} else {
				Element element = cache.get(key);
				if (element == null) {
					if (log.isDebugEnabled()) {
						log.debug("Element for " + key + " is null");
					}
					return null;
				} else {
					return (V) element.getObjectValue();
				}
			}
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
	}

	@Override
	public String getCacheName() {
		return this.cacheName;
	}

	@Override
	public long getCountInMemory() {

		return cache.getMemoryStoreSize();
	}

	
	@Override
	public long getSizeInMemory() {
		// TODO Auto-generated method stub
//		System.out.println("MemoryStoreSize:" + cache.getMemoryStoreSize());
//		System.out.println("calculateInMemorySize():" + cache.calculateInMemorySize());
//		System.out.println("Statistics:" + cache.getStatistics().toString());
		return cache.calculateInMemorySize();
	}

	@Override
	public V put(Object key, Object value) {
		try {
			Element element = new Element(key, value);
			cache.put(element);
		} catch (IllegalArgumentException e) {
			throw new CacheException(e);
		} catch (IllegalStateException e) {
			throw new CacheException(e);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
		return null;
	}

	@Override
	public V remove(Object key) {
		try {
			cache.remove(key);
		} catch (ClassCastException e) {
			throw new CacheException(e);
		} catch (IllegalStateException e) {
			throw new CacheException(e);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
		return null;
	}

	@Override
	public void update(Object key, Object value) {
		this.put(key, value);
	}

	@Override
	public V put(K key, V value, int ttl) {
		try {
			Element element = new Element(key, value);
			element.setTimeToIdle(ttl);
			cache.put(element);
		} catch (IllegalArgumentException e) {
			throw new CacheException(e);
		} catch (IllegalStateException e) {
			throw new CacheException(e);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
		return null;
	}



}
