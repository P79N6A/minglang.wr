package com.taobao.cun.auge.qualification.service;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
/***
 * 新入住流程请求
 * @author zhenhuan.zhangzh
 *
 */

public class C2BSettlingRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1708269599708743108L;

	/**
	 * 淘宝账号
	 */
	@NotNull
	private Long taobaoUserId;

	/**
	 * 流程版本
	 */
	private String version;
	
	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
	
}
