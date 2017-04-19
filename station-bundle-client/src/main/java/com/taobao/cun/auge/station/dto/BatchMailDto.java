package com.taobao.cun.auge.station.dto;

import java.util.List;
import java.util.Map;

public class BatchMailDto {

	private List<String> mailAddresses;
	
	private String templateId;
	
	private Map<String, String> contentMap;
	
	private String operator;

	public List<String> getMailAddresses() {
		return mailAddresses;
	}

	public void setMailAddresses(List<String> mailAddresses) {
		this.mailAddresses = mailAddresses;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Map<String, String> getContentMap() {
		return contentMap;
	}

	public void setContentMap(Map<String, String> contentMap) {
		this.contentMap = contentMap;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
