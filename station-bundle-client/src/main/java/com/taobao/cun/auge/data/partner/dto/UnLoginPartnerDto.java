package com.taobao.cun.auge.data.partner.dto;

public class UnLoginPartnerDto {
	private String taobaoUserId;
	
	private Long stationId;
	
	private String stationName;
	
	private String type;
	
	private String state;
	
	private String orgId;
	
	private String orgFullNamePath;

	public String getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(String taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgFullNamePath() {
		return orgFullNamePath;
	}

	public void setOrgFullNamePath(String orgFullNamePath) {
		this.orgFullNamePath = orgFullNamePath;
	}
}
