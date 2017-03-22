package com.taobao.cun.auge.station.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;

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
	
	/**
	 * 站点装修类型
	 */
	private StationDecorateTypeEnum stationDecorateTypeEnum;

	/**
	 * 站点装修出资类型
	 */
	private StationDecoratePaymentTypeEnum stationDecoratePaymentTypeEnum;

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

	public StationDecorateTypeEnum getStationDecorateTypeEnum() {
		return stationDecorateTypeEnum;
	}

	public void setStationDecorateTypeEnum(StationDecorateTypeEnum stationDecorateTypeEnum) {
		this.stationDecorateTypeEnum = stationDecorateTypeEnum;
	}

	public StationDecoratePaymentTypeEnum getStationDecoratePaymentTypeEnum() {
		return stationDecoratePaymentTypeEnum;
	}

	public void setStationDecoratePaymentTypeEnum(StationDecoratePaymentTypeEnum stationDecoratePaymentTypeEnum) {
		this.stationDecoratePaymentTypeEnum = stationDecoratePaymentTypeEnum;
	}
}
