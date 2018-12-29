package com.taobao.cun.auge.level.dto;

import java.io.Serializable;

public class TownLevelStationRuleDto implements Serializable{

	private static final long serialVersionUID = -8227370464828207975L;

	private String level;
	
	private String stationTypeCode;
	
	private String stationTypeDesc;
	
	public String getStationTypeCode() {
		return stationTypeCode;
	}

	public void setStationTypeCode(String stationTypeCode) {
		this.stationTypeCode = stationTypeCode;
	}

	public String getStationTypeDesc() {
		return stationTypeDesc;
	}

	public void setStationTypeDesc(String stationTypeDesc) {
		this.stationTypeDesc = stationTypeDesc;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
