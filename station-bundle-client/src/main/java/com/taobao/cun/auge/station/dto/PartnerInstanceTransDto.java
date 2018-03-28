package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

public class PartnerInstanceTransDto extends OperatorDto implements Serializable {

	private static final long serialVersionUID = 5575424342867122921L;

	private Long instanceId;
	
	/**
	 * 村点dto
	 */
	private StationDto stationDto;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public StationDto getStationDto() {
		return stationDto;
	}

	public void setStationDto(StationDto stationDto) {
		this.stationDto = stationDto;
	}
	
	

}
