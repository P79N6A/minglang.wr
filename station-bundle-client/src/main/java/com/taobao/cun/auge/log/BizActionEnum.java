package com.taobao.cun.auge.log;

public enum BizActionEnum {
	//县点
	countystation_create("创建县服务中心"),
	countystation_open("县点开业"),
	countystation_transfer_finished("县点交接完成"),
	countystation_auto_transfer_finished("县点自动交接完成"), //n+75天系统自动交接
	countystation_operate("县点运营"),
	//服务站
	station_create("创建服务站"),
	station_open("服务站开业"),
	station_transfer_finished("服务站交接完成"),
	station_auto_transfer_finished("服务站自动交接完成"); //n+75天系统自动交接
	
	public String desc;
	
	BizActionEnum(String desc){
		this.desc = desc;
	}
}
