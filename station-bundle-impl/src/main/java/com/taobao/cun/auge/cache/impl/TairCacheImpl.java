package com.taobao.cun.auge.cache.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cache.TairCache;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.TairManager;

@Component("tairCache")
public class TairCacheImpl implements TairCache {
	private static final Logger logger = LoggerFactory.getLogger(TairCacheImpl.class);
	@Autowired
	private TairManager tairManager;
	@Value("${tair.namespace}")
	private int tairNamespace;

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T get(String key) {
			Result<DataEntry> result = tairManager.get(this.tairNamespace, key);
			if (ResultCode.SUCCESS.equals(result.getRc())) {
				return (T) result.getValue().getValue();
			} else {
				logger.debug("get(String) failed, key=[{}], Result:{}", key, result);
			}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T get(String key, int expireTime) {
			Result<DataEntry> result = tairManager.get(this.tairNamespace, key, expireTime);
			if (ResultCode.SUCCESS.equals(result.getRc())) {
				return (T) result.getValue().getValue();
			} else {
				logger.debug("get(String, int) failed, key=[{}], Result:{}", key, result);
			}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> Map<String, T> mget(List<String> keyList) {
			Result<List<DataEntry>> result = tairManager.mget(tairNamespace, keyList);
			if (ResultCode.SUCCESS.equals(result.getRc())) {
				List<DataEntry> entryList = result.getValue();
				if (entryList != null && entryList.size() > 0) {
					Map<String, T> resultMap = new HashMap<String, T>();
					for (DataEntry entry : entryList) {
						resultMap.put((String) entry.getKey(), (T) entry.getValue());
					}
					return resultMap;
				}
			} else {
				logger.debug("mget(List<String>) failed, keyList={}, Result:{}", keyList, result);
			}
		return null;
	}

	@Override
	public void put(String key, Serializable value) {
		put(key, value, 0);
	}

	@Override
	public void put(String key, Serializable value, int expireTime) {
		ResultCode resultCode = tairManager.put(tairNamespace, key, value, 0, expireTime);
		if (!resultCode.isSuccess()) {
			throw new RuntimeException("put fail, resultCode=" + resultCode);
		}
	}

	@Override
	public void invalid(String key) {
		ResultCode resultCode = tairManager.invalid(tairNamespace, key);
		if (!resultCode.isSuccess()) {
			throw new RuntimeException("delete(String) fail, key=[" + key + "], resultCode=[" + resultCode + "]");
		}
	}

	@Override
	public void minvalid(List<String> keys) {
		ResultCode resultCode = tairManager.minvalid(tairNamespace, keys);
		if (!resultCode.isSuccess()) {
			throw new RuntimeException("delete(String) fail, key=[" + keys + "], resultCode=[" + resultCode + "]");
		}
	}
	
	@Override
	public Integer incr(Serializable key, int value, int defaultValue,
						int expireTime) {
		Result<Integer> result = tairManager.incr(tairNamespace, key, value,
				defaultValue, expireTime);
		if (!result.isSuccess()) {
			throw new RuntimeException("incr(String) fail, key=[" + key
					+ "], resultCode=[" + result + "]");
		}
		return result.getValue();
	}

}
