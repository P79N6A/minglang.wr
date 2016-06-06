package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 更新使用中（包含装修中，服务中，停业申请中）的实例Dto
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceUpdateServicingDto extends OperatorDto implements Serializable {
	
	private static final long serialVersionUID = -2598769021772108665L;

	/**
	 * 主键
	 */
	private Long id;

	private StationUpdateServicingDto stationDto;

	private PartnerUpdateServicingDto partnerDto;
	
	/**
	 * 乐观锁版本
	 */
	private Long version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StationUpdateServicingDto getStationDto() {
		return stationDto;
	}

	public void setStationDto(StationUpdateServicingDto stationDto) {
		this.stationDto = stationDto;
	}

	public PartnerUpdateServicingDto getPartnerDto() {
		return partnerDto;
	}

	public void setPartnerDto(PartnerUpdateServicingDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
