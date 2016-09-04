package com.taobao.cun.auge.station.condition;

import java.io.Serializable;

public class StationStatisticsCondition implements Serializable{

	private static final long serialVersionUID = 6132005680479470741L;
	
	private String orgId;
	private String stationName;
	private String state;
	private String taobaoUserName;
	private String province;
	private String city;
	private String county;
	private String town;
	private String stationNum;
	private String applierName;
	private String type;
	private String parentStationId;
	private String level;
	
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTaobaoUserName() {
		return taobaoUserName;
	}
	public void setTaobaoUserName(String taobaoUserName) {
		this.taobaoUserName = taobaoUserName;
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
	public String getStationNum() {
		return stationNum;
	}
	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}
	public String getApplierName() {
		return applierName;
	}
	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParentStationId() {
		return parentStationId;
	}
	public void setParentStationId(String parentStationId) {
		this.parentStationId = parentStationId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}
