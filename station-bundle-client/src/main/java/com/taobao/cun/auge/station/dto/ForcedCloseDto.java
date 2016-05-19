package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.StationForcedCloseReasonEnum;

public class ForcedCloseDto extends BaseDto implements Serializable {

	private static final long serialVersionUID = -2259932744519518324L;

	// 合伙人实例id
	private Long stationApplyId;

	private StationForcedCloseReasonEnum reason;

	public Long getStationApplyId() {
		return stationApplyId;
	}

	public void setStationApplyId(Long stationApplyId) {
		this.stationApplyId = stationApplyId;
	}

	public StationForcedCloseReasonEnum getReason() {
		return reason;
	}
}
