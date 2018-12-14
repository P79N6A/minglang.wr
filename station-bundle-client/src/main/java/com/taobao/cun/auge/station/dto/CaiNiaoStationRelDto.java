package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class CaiNiaoStationRelDto implements Serializable {

	private static final long serialVersionUID = 7558264140484128372L;

	private Long id;

    private Long objectId;

    private Long cainiaoStationId;

    private String type;

    private String isOwn;

    private Long logisticsStationId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
}
