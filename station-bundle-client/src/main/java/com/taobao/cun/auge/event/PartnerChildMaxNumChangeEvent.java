package com.taobao.cun.auge.event;

import com.taobao.cun.auge.common.OperatorDto;

public class PartnerChildMaxNumChangeEvent extends OperatorDto {

	private static final long serialVersionUID = 2818364638108816820L;

	/**
	 * 实例id,内部使用，外部系统不要使用
	 */
	private Long partnerInstanceId;
	
	private String bizMonth;
	
	private Integer childMaxNum;
	
	//变更原因
	private String reason;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public String getBizMonth() {
		return bizMonth;
	}

	public void setBizMonth(String bizMonth) {
		this.bizMonth = bizMonth;
	}

	public Integer getChildMaxNum() {
		return childMaxNum;
	}

	public void setChildMaxNum(Integer childMaxNum) {
		this.childMaxNum = childMaxNum;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
