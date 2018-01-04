package com.taobao.cun.auge.store.dto;

@Deprecated
/**
 * 请用StoreDto.category字段代替
 * @author zhenhuan.zhangzh
 *
 */
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
