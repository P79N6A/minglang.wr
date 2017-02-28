package com.taobao.cun.auge.testuser;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "testuser")
public class TestUserProperties {

    private Map<String, Map<String,String>> configs = new HashMap<>();

	public Map<String, Map<String, String>> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<String, Map<String, String>> configs) {
		this.configs = configs;
	}



}
