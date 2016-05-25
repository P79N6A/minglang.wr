package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class StartProcessDto implements Serializable {

	private static final long serialVersionUID = 3154749325747973905L;

	// 业务code
	private String businessCode;

	// 业务主键id
	private Long businessId;

	// 申请人工号，或者淘宝userid
	private String applierId;

	// 申请人所属村淘组织
	private Long applierOrgId;

	// 申请备注
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

	public Long getApplierOrgId() {
		return applierOrgId;
	}

	public void setApplierOrgId(Long applierOrgId) {
		this.applierOrgId = applierOrgId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
