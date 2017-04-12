package com.taobao.cun.auge.qualification.service;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.ali.com.google.common.collect.HashBiMap;

@ConfigurationProperties(prefix = "c2berror")
public class ErrorMessageProperties {
	
	private Map<String, String> messages;

	private Map<String, String> fixedErrorMessageCodes;
			
	public String getFixedErrorMessageCode(String errorMessage){
		if (fixedErrorMessageCodes != null) {
			return HashBiMap.create(fixedErrorMessageCodes).inverse().get(errorMessage);
		}
		return null;
	}
	
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
	
	public Map<String, String> getFixedErrorMessageCodes() {
		return fixedErrorMessageCodes;
	}

	public void setFixedErrorMessageCodes(Map<String, String> fixedErrorMessageCodes) {
		this.fixedErrorMessageCodes = fixedErrorMessageCodes;
	}

}
