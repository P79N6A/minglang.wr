package com.taobao.cun.auge.station.transfer.dto;

public enum CountyStationTransferPhase {
	COUNTY_NOT_TRANS("县域未交接"),
	COUNTY_TRANSING("县域交接中"),
	COUNTY_TRANSED("县域已交接"),
	COUNTY_AUTO_TRANSED("N+75交接完毕");
	
	private String desc;
	
	private CountyStationTransferPhase(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
