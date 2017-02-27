package com.taobao.cun.auge.settling;

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

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}
	
	
	
	
}
