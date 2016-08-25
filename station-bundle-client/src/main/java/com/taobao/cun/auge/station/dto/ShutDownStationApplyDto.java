package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;

public class ShutDownStationApplyDto extends OperatorDto{

	private static final long serialVersionUID = 5198576840053716625L;
	
	//村点id
	private Long stationId;
	
	//撤点原因
	private String reason;

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
