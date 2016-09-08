package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;

public class StartProcessDto extends OperatorDto {

	private static final long serialVersionUID = 3154749325747973905L;

	// 业务code
	private String businessCode;

	// 业务主键id
	private Long businessId;

	// 申请备注
	private String remarks;

	// 重构，兼容
	private Boolean isInstanceId;

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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean isInstanceId() {
		return isInstanceId;
	}

	public void setInstanceId(Boolean isInstanceId) {
		this.isInstanceId = isInstanceId;
	}
}
