package com.taobao.cun.auge.level.dto;

import java.io.Serializable;

public class TownLevelStationRuleDto implements Serializable{

	private static final long serialVersionUID = -8227370464828207975L;

	private String level;
	
	private String stationTypeCode;
	
	private String stationTypeDesc;
	
	private String areaCode;
	
	private String rule;
	
	private String ruleData;
	
	public String getRuleData() {
		return ruleData;
	}

	public void setRuleData(String ruleData) {
		this.ruleData = ruleData;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
