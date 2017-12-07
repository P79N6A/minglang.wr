package com.taobao.cun.auge.qualification.service;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "qualiAbnormal")
public class QualiAbnormalMessageProperties {

	public Map<String, String> getMessages() {
		return messages;
	}

	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

	private Map<String, String> messages;
	
	
	public String getAbnormalMessage(String key){
		return messages.get(key);
	}
}
