package com.taobao.cun.auge.service.impl;

import javax.validation.constraints.NotNull;

public class TestBean {

	@NotNull
	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}
