package com.taobao.cun.auge.station.response;

import java.io.Serializable;

public class TpaApplyResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4493176896002983329L;

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
