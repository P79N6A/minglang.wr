package com.taobao.cun.auge.level.dto;

import java.io.Serializable;

public class TownLevelStationSetting implements Serializable{

	private static final long serialVersionUID = -6143805209197195790L;

	private String level;
	
	private String stationTypeCode;
	
	private String stationTypeDesc;
	
	private String areaCode;

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

	public String getStationTypeDesc() {
		return stationTypeDesc;
	}

	public void setStationTypeDesc(String stationTypeDesc) {
		this.stationTypeDesc = stationTypeDesc;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
}
