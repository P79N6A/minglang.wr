package com.taobao.cun.auge.failure;

import org.springframework.boot.diagnostics.FailureAnalysis;

public class AugeFailureAnalysis extends FailureAnalysis {

	public AugeFailureAnalysis(String description, String action, Throwable cause) {
		super(description, action, cause);
	}
	
	private boolean isFatal;
	
	private boolean isBusinessException;
	

	private String parameters;
	
	private String lineNum;
	
	private String bizType;
	
	public boolean isFatal() {
		return isFatal;
	}

	public void setFatal(boolean isFatal) {
		this.isFatal = isFatal;
	}
	
	
	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getLineNum() {
		return lineNum;
	}

	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public boolean isBusinessException() {
		return isBusinessException;
	}

	public void setBusinessException(boolean isBusinessException) {
		this.isBusinessException = isBusinessException;
	}

}
