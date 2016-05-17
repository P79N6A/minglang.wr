package com.taobao.cun.auge.event.domain;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.crius.event.annotation.EventDesc;
import com.taobao.cun.crius.event.annotation.EventField;

@EventDesc("station-state-changed-event")
public class StationStatusChangedEvent implements Serializable {

	private static final long serialVersionUID = 3410023449786716039L;

	@EventField("村点id")
	private Long stationId;
	
	@EventField("新的状态")
	private StationStatusEnum newStatus;
	
	@EventField("新的状态")
	private StationStatusEnum oldStatus;

	@EventField("变更时间")
	private Date changedTime;
	
	@EventField("村点当前合伙人淘宝账号")
	private Long taobaoUserId;
	
	@EventField("村点当前合伙人类型")
	private PartnerInstanceTypeEnum partnerType;
	
	@EventField("操作人")
	private String operatorId;

	@EventField("操作类型")
	private String operatorType;

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}


	public Date getChangedTime() {
		return changedTime;
	}

	public void setChangedTime(Date changedTime) {
		this.changedTime = changedTime;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public PartnerInstanceTypeEnum getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(PartnerInstanceTypeEnum partnerType) {
		this.partnerType = partnerType;
	}

	public StationStatusEnum getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(StationStatusEnum newStatus) {
		this.newStatus = newStatus;
	}

	public StationStatusEnum getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(StationStatusEnum oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}	
}
