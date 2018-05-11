package com.taobao.cun.auge.testuser;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestUserInfo {

	private Map<String,String> config;
	
	private List<Long> taobaoUserIds;

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}

	public List<Long> getTaobaoUserIds() {
		return taobaoUserIds;
	}

	public void setTaobaoUserIds(List<Long> taobaoUserIds) {
		this.taobaoUserIds = taobaoUserIds;
	}
	
	
	public static void main(String[] args) {
		TestUserInfo info = new TestUserInfo();
		Map<String,String> config = Maps.newHashMap();
		config.put("date", "xxxxx");
		info.setConfig(config);
		info.setTaobaoUserIds(Lists.newArrayList(1234l));
		System.out.println(JSON.toJSONString(info));
	}
}
