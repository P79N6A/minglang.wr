package com.taobao.cun.auge.company.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class VendorQueryPageCondition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -973691463932246143L;

	private String companyName;
	
	private String taobaoNick;
	
	private String mobile;
	
	private String state;

	@NotNull(message="pageNum is null")
	private int pageNum = 1;
	
	@NotNull(message="pageSize is null")
	private int pageSize = 10;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
