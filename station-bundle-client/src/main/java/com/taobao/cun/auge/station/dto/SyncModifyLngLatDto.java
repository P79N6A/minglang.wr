package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * @author alibaba-jinwang
 * 修改经纬度同步菜鸟Dto
 */
public class SyncModifyLngLatDto extends OperatorDto implements Serializable{

	private static final long serialVersionUID = 2674396348329942505L;
	
	/**
	 * 合伙人实例id
	 */
	private Long partnerInstanceId;
	
	private String lng;
	
	private String lat;
	
	private Long cainiaoStationId;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
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

	public Long getCainiaoStationId() {
		return cainiaoStationId;
	}

	public void setCainiaoStationId(Long cainiaoStationId) {
		this.cainiaoStationId = cainiaoStationId;
	}
}
