package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;

public class ApproveProcessTask extends OperatorDto implements Serializable {

	private static final long serialVersionUID = -7648340339057681585L;

	@NotNull(message = "business not null")
	private ProcessBusinessEnum business;

	@NotNull(message = "businessId not null")
	private Long businessId;

	private String businessName;
	
	private Long businessOrgId;

	private Map<String, String> params = new HashMap<String, String>();
	
	public Long getBusinessOrgId() {
		return businessOrgId;
	}

	public void setBusinessOrgId(Long businessOrgId) {
		this.businessOrgId = businessOrgId;
	}

	public ProcessBusinessEnum getBusiness() {
		return business;
	}

	public void setBusiness(ProcessBusinessEnum business) {
		this.business = business;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}