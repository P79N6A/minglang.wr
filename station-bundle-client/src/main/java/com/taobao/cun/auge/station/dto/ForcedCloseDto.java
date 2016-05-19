package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.StationForcedCloseReasonEnum;

public class ForcedCloseDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -2259932744519518324L;
	// 合伙人实例id
	private Long instanceId;

	private StationForcedCloseReasonEnum reason;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public StationForcedCloseReasonEnum getReason() {
		return reason;
	}
}