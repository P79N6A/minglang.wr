package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;

public class CuntaoCainiaoStationRelDto  extends BaseDto implements Serializable  {

	private static final long serialVersionUID = 4896621717590920987L;

    private Long objectId;

    /**
     * 菜鸟id
     */
    private Long cainiaoStationId;

    private CuntaoCainiaoStationRelTypeEnum type;

    /**
     * 是否是自己的物流站
     */
    private String isOwn;

    /**
     * 菜鸟物流站主键id
     */
    private Long logisticsStationId;

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getCainiaoStationId() {
		return cainiaoStationId;
	}

	public void setCainiaoStationId(Long cainiaoStationId) {
		this.cainiaoStationId = cainiaoStationId;
	}

	public String getIsOwn() {
		return isOwn;
	}

	public void setIsOwn(String isOwn) {
		this.isOwn = isOwn;
	}

	public Long getLogisticsStationId() {
		return logisticsStationId;
	}

	public void setLogisticsStationId(Long logisticsStationId) {
		this.logisticsStationId = logisticsStationId;
	}

	public CuntaoCainiaoStationRelTypeEnum getType() {
		return type;
	}

	public void setType(CuntaoCainiaoStationRelTypeEnum type) {
		this.type = type;
	}
}
