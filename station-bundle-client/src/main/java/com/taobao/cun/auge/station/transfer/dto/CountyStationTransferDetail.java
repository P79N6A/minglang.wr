package com.taobao.cun.auge.station.transfer.dto;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.station.dto.CountyStationDto;
import com.taobao.cun.auge.station.dto.StationDto;

public class CountyStationTransferDetail {
	private Long id;

    private Long targetTeamOrgId;
    
    private String targetTeamOrgFullNamePath;

    private CountyStationDto countyStation;

    private List<StationDto> stations;

    private List<String> attachments;
    
    private  String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTargetTeamOrgId() {
		return targetTeamOrgId;
	}

	public void setTargetTeamOrgId(Long targetTeamOrgId) {
		this.targetTeamOrgId = targetTeamOrgId;
	}

	public String getTargetTeamOrgFullNamePath() {
		return targetTeamOrgFullNamePath;
	}

	public void setTargetTeamOrgFullNamePath(String targetTeamOrgFullNamePath) {
		this.targetTeamOrgFullNamePath = targetTeamOrgFullNamePath;
	}

	public CountyStationDto getCountyStation() {
		return countyStation;
	}

	public void setCountyStation(CountyStationDto countyStation) {
		this.countyStation = countyStation;
	}

	public List<StationDto> getStations() {
		return stations;
	}

	public void setStations(List<StationDto> stations) {
		this.stations = stations;
	}

	public List<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}
	
	public List<Long> getStationIds(){
		List<Long> ids = new ArrayList<Long>();
		for(StationDto station : stations) {
			ids.add(station.getId());
		}
		return ids;
	}
}
