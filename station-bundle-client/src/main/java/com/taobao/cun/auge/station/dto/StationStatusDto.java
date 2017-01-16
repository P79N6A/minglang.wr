package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.station.enums.ProcessedStationStatusEnum;

public class StationStatusDto {

	private ProcessedStationStatusEnum status;
	
	private Integer count;
	
	public ProcessedStationStatusEnum getStatus() {
		return status;
	}
	public void setStatus(ProcessedStationStatusEnum status) {
		this.status = status;
	}
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

}
