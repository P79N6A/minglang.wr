package com.taobao.cun.auge.qualification.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "c2b.error")
public class ErrorMessageProperties {

	private Map<String, String> messages = new HashMap<>();

	public Map<String, String> getMessages() {
		return messages;
	}

	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

	public String getErrorMessage(String errorCode) {
		if (messages != null) {
			return messages.get(errorCode);
		}
		return null;
	}
	
}
