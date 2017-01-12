package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;

public class StartProcessDto extends OperatorDto {

	private static final long serialVersionUID = 3154749325747973905L;

	// 业务类型
	private ProcessBusinessEnum business;
	
	// 业务code
	private String businessCode;

	// 业务主键id
	private Long businessId;
	
	private Long businessOrgId;
	
	//申请单id
	private Long applyId;

	//业务名称
	private String businessName;

	// 重构，兼容
	private Long partnerInstanceId;
	
	//业务自定义的参数
	private String jsonParams;
	
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

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public String getJsonParams() {
		return jsonParams;
	}

	public void setJsonParams(String jsonParams) {
		this.jsonParams = jsonParams;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}
}
