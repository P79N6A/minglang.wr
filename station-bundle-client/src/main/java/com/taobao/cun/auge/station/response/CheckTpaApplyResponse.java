package com.taobao.cun.auge.station.response;

import java.io.Serializable;

public class CheckTpaApplyResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7827938955304396629L;

	private boolean success;
	
	private String errorMessage;
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
}
