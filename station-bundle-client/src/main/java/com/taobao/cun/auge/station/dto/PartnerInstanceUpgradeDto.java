package com.taobao.cun.auge.station.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

public class PartnerInstanceUpgradeDto extends OperatorDto {

	private static final long serialVersionUID = 6251152939020905519L;

	/**
	 * 村点dto
	 */
	@NotNull(message = "StationDto not null")
	private StationDto stationDto;

	/**
	 * 合伙人dto
	 */
	@NotNull(message = "PartnerDto not null")
	private PartnerDto partnerDto;

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
