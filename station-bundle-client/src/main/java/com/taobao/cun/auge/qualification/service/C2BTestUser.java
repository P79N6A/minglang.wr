package com.taobao.cun.auge.qualification.service;

import java.io.Serializable;

public class C2BTestUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5924531825940251052L;

	private Long taobaoUserId;

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}
	
}
