package com.taobao.cun.auge.station.dto;

public class StationStatisticDto {
	
	private StationEnterStatusDto enterFlow;
	
	private StationQuitStatusDto quitFlow;

	public StationEnterStatusDto getEnterFlow() {
		return enterFlow;
	}

	public void setEnterFlow(StationEnterStatusDto enterFlow) {
		this.enterFlow = enterFlow;
	}

	public StationQuitStatusDto getQuitFlow() {
		return quitFlow;
	}

	public void setQuitFlow(StationQuitStatusDto quitFlow) {
		this.quitFlow = quitFlow;
	}
}
