package com.taobao.cun.auge.station.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.StationForcedCloseReasonEnum;

public class ForcedCloseDto extends OperatorDto {

	private static final long serialVersionUID = -2259932744519518324L;
	
	// 合伙人实例id
	@NotNull(message="instanceId not null")
	private Long instanceId;

	private StationForcedCloseReasonEnum reason;
	
	private String remarks;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public StationForcedCloseReasonEnum getReason() {
		return reason;
	}

	public void setReason(StationForcedCloseReasonEnum reason) {
		this.reason = reason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
