package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;

public class PartnerProtocolDto implements Serializable{

	private static final long serialVersionUID = 1309307708354842264L;

	private ProtocolDto protocol;
	
	private Long taobaoUserId;
	
    private Long protocolId;

	private Date confirmTime;

	private Date startTime;

	private Date endTime;

	private Long objectId;

	private PartnerProtocolRelTargetTypeEnum targetType;

	public ProtocolDto getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolDto protocol) {
		this.protocol = protocol;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public PartnerProtocolRelTargetTypeEnum getTargetType() {
		return targetType;
	}

	public void setTargetType(PartnerProtocolRelTargetTypeEnum targetType) {
		this.targetType = targetType;
	}

	public Long getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Long protocolId) {
		this.protocolId = protocolId;
	}
	
	
}
