package com.taobao.cun.auge.station.transfer.dto;

import java.io.Serializable;
import java.util.List;

import com.taobao.cun.auge.station.dto.CountyStationDto;

public class CountyStationTransferCondition implements Serializable{
	private static final long serialVersionUID = -305450898700523643L;

	private boolean countyTransfer;
	
	private boolean stationTransfer;
	
	private CountyStationDto countyStationDto;
	
	/**
	 * 待转交的已开业村点
	 */
	private List<TransferStation> transferStations;
	
	private String memo;
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public boolean isCountyTransfer() {
		return countyTransfer;
	}
	public void setCountyTransfer(boolean countyTransfer) {
		this.countyTransfer = countyTransfer;
	}
	public boolean isStationTransfer() {
		return stationTransfer;
	}
	public void setStationTransfer(boolean stationTransfer) {
		this.stationTransfer = stationTransfer;
	}
	public CountyStationDto getCountyStationDto() {
		return countyStationDto;
	}
	public void setCountyStationDto(CountyStationDto countyStationDto) {
		this.countyStationDto = countyStationDto;
	}
	public List<TransferStation> getTransferStations() {
		return transferStations;
	}
	public void setTransferStations(List<TransferStation> transferStations) {
		this.transferStations = transferStations;
	}
}
