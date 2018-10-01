package com.taobao.cun.auge.station.transfer.dto;

/**
 * 村点或者站点交接状态
 * 
 * @author chengyu.zhoucy
 *
 */
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
