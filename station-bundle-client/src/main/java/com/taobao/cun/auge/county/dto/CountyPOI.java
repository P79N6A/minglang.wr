package com.taobao.cun.auge.county.dto;

import java.io.Serializable;

public class CountyPOI implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8096844560714648176L;
	
	private String lng;
	
	private String lat;
	
	private Long cainaoStationId;

	public Long getCainaoStationId() {
		return cainaoStationId;
	}

	public void setCainaoStationId(Long cainaoStationId) {
		this.cainaoStationId = cainaoStationId;
	}
	
	
	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
	

	
	
	

}
