package com.taobao.cun.auge.level.dto;

import java.io.Serializable;

public class TownLevelRuleDto implements Serializable{

	private static final long serialVersionUID = -166729439273276794L;

	private String level;
	
	private String levelRule;
	
	private String areaCode;
	
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getLevelRule() {
		return levelRule;
	}

	public void setLevelRule(String levelRule) {
		this.levelRule = levelRule;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
