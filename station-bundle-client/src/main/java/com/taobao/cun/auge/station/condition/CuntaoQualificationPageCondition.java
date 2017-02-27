package com.taobao.cun.auge.station.condition;

import java.util.List;

import com.taobao.cun.auge.common.PageQuery;

public class CuntaoQualificationPageCondition extends PageQuery{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8325075310656303204L;

	private List<String> userTypes;
	
	private List<Long> orgIds;
	
	private List<String> invalidPartnerInstanceStatus;
	

	public List<String> getUserTypes() {
		return userTypes;
	}

	public void setUserTypes(List<String> userTypes) {
		this.userTypes = userTypes;
	}

	public List<Long> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}

	public List<String> getInvalidPartnerInstanceStatus() {
		return invalidPartnerInstanceStatus;
	}

	public void setInvalidPartnerInstanceStatus(List<String> invalidPartnerInstanceStatus) {
		this.invalidPartnerInstanceStatus = invalidPartnerInstanceStatus;
	}

	
	
}
