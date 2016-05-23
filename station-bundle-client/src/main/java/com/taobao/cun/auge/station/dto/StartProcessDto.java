package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class StartProcessDto implements Serializable{

	private static final long serialVersionUID = 3154749325747973905L;
	
	private String businessCode;
	private Long businessId;
	private String applierId;
	private Long parentOrgId;
	private String remarks;
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public String getApplierId() {
		return applierId;
	}
	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}
	public Long getParentOrgId() {
		return parentOrgId;
	}
	public void setParentOrgId(Long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
