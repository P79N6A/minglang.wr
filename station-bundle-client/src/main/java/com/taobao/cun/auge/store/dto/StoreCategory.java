package com.taobao.cun.auge.store.dto;

public enum StoreCategory {

	FMCG("FMCG","快消"),MOMBABY("MOMBABY","母婴"),ELEC("ELEC","家电"),SUPPLY("SUPPLY","村点补货");
	
	private StoreCategory(String category,String desc){
		this.category = category;
		this.desc = desc;
	}
	
	private String category;

	private String desc;
	
	public String getCategory() {
		return category;
	}

	public String getDesc() {
		return desc;
	}
	
	
}
