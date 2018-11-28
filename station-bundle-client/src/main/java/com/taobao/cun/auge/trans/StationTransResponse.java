package com.taobao.cun.auge.trans;

import java.io.Serializable;

/**
 * 转型响应接口
 * @author xiaoxiong.xx
 *
 */
public class StationTransResponse implements Serializable{

	private static final long serialVersionUID = -8073163150551017605L;

	/**
	 * 是否成功
	 */
	private boolean successful;

	/**
	 * 错误消息
	 */
	private String errorMessage;
	
	/**
	 * 错误码
	 */
	private String errorCode;

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

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
