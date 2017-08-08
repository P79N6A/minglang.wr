package com.taobao.cun.auge.station.response;

import java.io.Serializable;
import java.util.List;

public class TpaListQueryResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2664332996727052689L;

	private boolean success;
	
	private String errorMessage;
	
	private List<TpaStationInfo> stations;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<TpaStationInfo> getStations() {
		return stations;
	}

	public void setStations(List<TpaStationInfo> stations) {
		this.stations = stations;
	}
	
}
