package com.taobao.cun.auge.level.dto;

public enum TownLevelEnum {
	A("A镇"),
	B("B镇"),
	C("C镇"),
	X("超标镇");
	
	private String desc;
	
	private TownLevelEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
