package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.StationTransInfoTypeEnum;

public class PartnerInstanceTransDto extends OperatorDto implements Serializable {

	private static final long serialVersionUID = 5575424342867122921L;
	/**
	 * 实例ID
	 */
	private Long instanceId;

	private String toBizType;
	
	/**
	 * 村点dto
	 */
	private StationDto stationDto;
	/**
	 * 转型类型
	 */
	private StationTransInfoTypeEnum type;
	

	public StationTransInfoTypeEnum getType() {
		return type;
	}

	public void setType(StationTransInfoTypeEnum type) {
		this.type = type;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getToBizType() {
		return toBizType;
	}

	public void setToBizType(String toBizType) {
		this.toBizType = toBizType;
	}

	public StationDto getStationDto() {
		return stationDto;
	}

	public void setStationDto(StationDto stationDto) {
		this.stationDto = stationDto;
	}
}
