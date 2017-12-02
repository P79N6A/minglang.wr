package com.taobao.cun.auge.company.dto;

import com.taobao.cun.auge.common.PageQuery;

public class CompanyQueryPageCondition extends PageQuery{

	/**
	 * 
	 */
	private static final long serialVersionUID = -973691463932246143L;

	private String companyName;
	
	private String taobaoNick;
	
	private String mobile;
	
	private String state;

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
