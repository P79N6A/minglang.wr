package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;

public class StartProcessDto extends OperatorDto {

	private static final long serialVersionUID = 3154749325747973905L;

	// 业务code
	private String businessCode;

	// 业务主键id
	private Long businessId;

	//申请单id
	private Long applyId;

	// 重构，兼容
	private Long partnerInstanceId;

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

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}
}
