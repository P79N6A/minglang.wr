package com.taobao.cun.auge.station.bo.dto;

import java.util.List;

/**
 * 为围栏查询站点的条件
 * 
 * @author chengyu.zhoucy
 *
 */
public class FenceStationQueryCondition {
	/**
	 * 组织路径
	 */
	private String fullIdPath;
	
	private String province;
	
	private String city;
	
	private String county;
	
	private String town;
	
	private List<String> stationTypes;
	
	private List<String> stationStatus;
	
	private List<String> stationLocations;
	
	public List<String> getStationLocations() {
		return stationLocations;
	}

	public void setStationLocations(List<String> stationLocations) {
		this.stationLocations = stationLocations;
	}

	public String getFullIdPath() {
		return fullIdPath;
	}

	public void setFullIdPath(String fullIdPath) {
		this.fullIdPath = fullIdPath;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public List<String> getStationTypes() {
		return stationTypes;
	}

	public void setStationTypes(List<String> stationTypes) {
		this.stationTypes = stationTypes;
	}

	public List<String> getStationStatus() {
		return stationStatus;
	}

	public void setStationStatus(List<String> stationStatus) {
		this.stationStatus = stationStatus;
	}
}
