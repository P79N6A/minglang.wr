package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class SyncTPDegreeCainiaoStationDto implements Serializable {

	private static final long serialVersionUID = 6790753156285391859L;
	private Long stationId;
	private Long taobaoUserId;
	private Long parentStationId;
	private Long parentTaobaoUserId;

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Long getParentStationId() {
		return parentStationId;
	}

	public void setParentStationId(Long parentStationId) {
		this.parentStationId = parentStationId;
	}

	public Long getParentTaobaoUserId() {
		return parentTaobaoUserId;
	}

	public void setParentTaobaoUserId(Long parentTaobaoUserId) {
		this.parentTaobaoUserId = parentTaobaoUserId;
	}

}
