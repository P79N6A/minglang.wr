package com.taobao.cun.auge.station.transfer.dto;

public class TransferJob {
	private Long countyStationId;
	
	private Long targetTeamOrgId;
	
	private String stationIdList;

    private String attachments;
    
    private String creator;
    
    private String type;
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Long getCountyStationId() {
		return countyStationId;
	}

	public void setCountyStationId(Long countyStationId) {
		this.countyStationId = countyStationId;
	}

	public Long getTargetTeamOrgId() {
		return targetTeamOrgId;
	}

	public void setTargetTeamOrgId(Long targetTeamOrgId) {
		this.targetTeamOrgId = targetTeamOrgId;
	}

	public String getStationIdList() {
		return stationIdList;
	}

	public void setStationIdList(String stationIdList) {
		this.stationIdList = stationIdList;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
}
