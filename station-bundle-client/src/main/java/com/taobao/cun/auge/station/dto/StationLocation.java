package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务站经纬度位置，由客户端采集回来
 * 
 * @author chengyu.zhoucy
 *
 */
public class StationLocation implements Serializable {

	private static final long serialVersionUID = -6248239015356418499L;
	
	private String stationId;
	
	private String lng;
	
	private String lat;
	
	private Date createTime;

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
