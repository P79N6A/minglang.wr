package com.taobao.cun.auge.store.dto;

import com.taobao.cun.auge.common.PageQuery;

public class StoreQueryPageCondition extends PageQuery{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5599291807605659873L;

	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
