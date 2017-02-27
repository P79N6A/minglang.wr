package com.taobao.cun.auge.settling;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
/***
 * 新入住流程请求
 * @author zhenhuan.zhangzh
 *
 */

public class C2BSignSettleProtocolResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1708269599708743108L;

	private boolean successful;
	
	private String errorMessage;

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
	
}
