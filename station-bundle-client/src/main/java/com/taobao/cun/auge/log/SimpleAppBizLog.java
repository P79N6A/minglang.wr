package com.taobao.cun.auge.log;

import java.io.Serializable;

/**
 * 简单日志
 * @author chengyu.zhoucy
 *
 */
public class SimpleAppBizLog implements Serializable{
	private static final long serialVersionUID = 3225225751075110070L;

	private String bizType;

    private Long bizKey;

    private String state;

    private String message;
    
    private String creator;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public Long getBizKey() {
		return bizKey;
	}

	public void setBizKey(Long bizKey) {
		this.bizKey = bizKey;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
