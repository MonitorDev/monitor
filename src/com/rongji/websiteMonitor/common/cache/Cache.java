package com.rongji.websiteMonitor.common.cache;


/**
 * Cache统一接口
 * <p>
 * Title: 榕基RJ-CMS
 * </p>
 * <p>
 * Description:缓存
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: 榕基软件开发有限公司
 * </p>
 * @author XFB
 * @param <K>
 * @param <V>
 */
public interface Cache<K,V> {
	
	/**
	 * 获取数据
	 * @param key
	 * @return
	 */
	public V get(Object key);
	/**
	 * 保存数据,若已存在则返回旧的数据
	 * @param key
	 * @param value
	 * @return
	 */
	public V put(K key,V value);
	
	
	
    /**
     * 移除元素
     * @param key
     * @return
     */
	public V remove(Object key);
	
	/**
	 * 修改元素
	 * @param key
	 * @param value
	 * @return
	 */
	public void update(Object key,Object value);
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param ttl 单位秒 
	 * @return
	 */
	public V put(K key,V value,int ttl);
	
	/**
	 * 清空缓存 
	 * @return
	 */
	public boolean clear();
	
	
	/**
	 * 缓存大小,返回占用字节数
	 * @return
	 */
	public long getSizeInMemory();
	
	/**
	 * 在内存中的个数
	 * @return
	 */
	public long getCountInMemory();
	
	/**
	 * 获取缓存名称
	 * @return
	 */
	public String getCacheName();


}
