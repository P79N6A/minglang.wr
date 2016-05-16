package com.taobao.cun.auge.event.domain;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.crius.event.annotation.EventDesc;
import com.taobao.cun.crius.event.annotation.EventField;

@EventDesc("station-state-changed-event")
public class StationStatusChangedEvent implements Serializable {

	private static final long serialVersionUID = 3410023449786716039L;

	@EventField("村点id")
	private Long stationId;

	@EventField("操作人")
	private String operatorId;

	@EventField("新的状态")
	private StationStatusEnum status;

	@EventField("变更事件")
	private Date changedTime;

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

	public StationStatusEnum getStatus() {
		return status;
	}

	public void setStatus(StationStatusEnum status) {
		this.status = status;
	}

	public Date getChangedTime() {
		return changedTime;
	}

	public void setChangedTime(Date changedTime) {
		this.changedTime = changedTime;
	}
}
