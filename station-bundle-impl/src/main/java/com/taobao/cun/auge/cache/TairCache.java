package com.taobao.cun.auge.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface TairCache {

	public <T extends Serializable> T get(String key);

	/**
	 * 获取指定的key值，并更新expireTime值，如果该值已过期，则无效
	 * 
	 * @param key
	 * @param expireTime
	 *            单位是秒
	 * @return
	 */
	public <T extends Serializable> T get(String key, int expireTime);

	/**
	 * 批量获取
	 * 
	 * @param keyList
	 * @return
	 */
	public <T extends Serializable> Map<String, T> mget(List<String> keyList);

	public void put(String key, Serializable value);

	/**
	 * 设置数据，如果数据已经存在，则覆盖，如果不存在，则新增
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 *            数据的有效时间，单位为秒
	 * @return
	 */
	public void put(String key, Serializable value, int expireTime);


	/**
	 * 缓存失效<br/>
	 * 可能会在多个机房中部署多个Tair集群，而用户的数据可能同时分布在这多个集群，<br/>
	 * 所以当删除时，除了要删除本地集群中数据，还要删除其他集群上的数据，以保证数据的一致性。
	 * 
	 * @param key
	 * @return
	 */
	void invalid(String key);

	/**
	 * 批量失效
	 * 
	 * @param keys
	 */
	void minvalid(List<String> keys);

}
