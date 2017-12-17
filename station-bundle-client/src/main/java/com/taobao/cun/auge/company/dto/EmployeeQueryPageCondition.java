package com.taobao.cun.auge.company.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class EmployeeQueryPageCondition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -973691463932246143L;

	private String name;
	
	private String taobaoNick;
	
	private String mobile;
	
	private Long vendorId;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}



}
