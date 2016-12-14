package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;

public class PartnerInstanceUpgradeDto extends OperatorDto {

	private static final long serialVersionUID = 6251152939020905519L;

	/**
	 * 主键
	 */
	private Long partnerInstanceId;

	/**
	 * 村点dto
	 */
	private StationDto stationDto;

	/**
	 * 合伙人dto
	 */
	private PartnerDto partnerDto;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public StationDto getStationDto() {
		return stationDto;
	}

	public void setStationDto(StationDto stationDto) {
		this.stationDto = stationDto;
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

}
