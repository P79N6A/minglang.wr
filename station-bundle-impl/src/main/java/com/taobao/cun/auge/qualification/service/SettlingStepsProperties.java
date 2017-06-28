package com.taobao.cun.auge.qualification.service;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "settling.steps")
public class SettlingStepsProperties {

	private  Map<String,String> version;

	public Map<String, String> getVersion() {
		return version;
	}

	public void setVersion(Map<String, String> version) {
		this.version = version;
	}

	
}
