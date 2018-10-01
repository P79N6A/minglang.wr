package com.taobao.cun.auge.station.transfer.dto;

public enum StationTransferPhase {
	STATION_NOT_TRANS("村点未交接"),
	STATION_TRANSING("村点交接中"),
	STATION_TRANSED("村点已交接");
	
	private String desc;
	
	private StationTransferPhase(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
