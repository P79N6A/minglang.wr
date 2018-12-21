package com.taobao.cun.auge.level.dto;

import java.io.Serializable;

public class TownLevelRuleDto implements Serializable{

	private static final long serialVersionUID = -166729439273276794L;

	private String level;
	
	private String stationTypeCode;
	
	private String stationTypeName;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStationTypeCode() {
		return stationTypeCode;
	}

	public void setStationTypeCode(String stationTypeCode) {
		this.stationTypeCode = stationTypeCode;
	}

	public String getStationTypeName() {
		return stationTypeName;
	}

	public void setStationTypeName(String stationTypeName) {
		this.stationTypeName = stationTypeName;
	}
}
