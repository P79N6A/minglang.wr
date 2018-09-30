package com.taobao.cun.auge.station.transfer;

public enum TransferState {
	WAITING("待交接"),
	TRANSFERING("交接中"),
	FINISHED("已交接");
	
	private String desc;
	
	private TransferState(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
