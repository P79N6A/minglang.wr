package com.taobao.cun.auge.store.dto;

public enum StoreStatus {
	HOLD("HOLD","暂停营业"),CLOSE("CLOSE","关店"),NORMAL("NORMAL","正常营业");
	
	private StoreStatus(String status,String desc){
		this.status = status;
		this.desc = desc;
	}
	
	private String status;

	private String desc;

	public String getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}
}
