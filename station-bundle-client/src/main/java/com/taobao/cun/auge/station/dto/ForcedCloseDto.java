package com.taobao.cun.auge.station.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;

public class ForcedCloseDto extends OperatorDto {

	private static final long serialVersionUID = -2259932744519518324L;
	
	// 合伙人实例id
	@NotNull(message="instanceId not null")
	private Long instanceId;

	@NotNull(message="PartnerForcedCloseReasonEnum not null")
	private CloseStationApplyCloseReasonEnum reason;
	
	private String remarks;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public CloseStationApplyCloseReasonEnum getReason() {
		return reason;
	}

	public void setReason(CloseStationApplyCloseReasonEnum reason) {
		this.reason = reason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
