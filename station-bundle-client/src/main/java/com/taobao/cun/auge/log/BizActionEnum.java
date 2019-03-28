package com.taobao.cun.auge.log;

public enum BizActionEnum {
	//县点
	countystation_create("创建县服务中心"),
	countystation_wait_open("县点待开业"),
	countystation_opening("县点开业"),
	//服务站
	station_create("创建服务站"),
	station_open("服务站开业");
	
	public String desc;
	
	BizActionEnum(String desc){
		this.desc = desc;
	}
}
