package com.taobao.cun.auge.station.transfer.dto;

import java.io.Serializable;
import java.util.Date;

public class TransferStation implements Serializable{
	private static final long serialVersionUID = 6086483054383491541L;

	private Long stationId;
	
	private String stationName;
	
	private String type;
	
	private String state;
	
	private Date openDate;
	
	private String transferState;

	public String getTransferState() {
		return transferState;
	}

	public void setTransferState(String transferState) {
		this.transferState = transferState;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
}
