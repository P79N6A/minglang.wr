package com.taobao.cun.auge.company.dto;

import com.taobao.cun.auge.common.PageQuery;

public class EmployeeQueryPageCondition extends PageQuery{

	/**
	 * 
	 */
	private static final long serialVersionUID = -973691463932246143L;

	private String name;
	
	private String taobaoNick;
	
	private String mobile;
	
	private Long companyId;
	
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

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

}
