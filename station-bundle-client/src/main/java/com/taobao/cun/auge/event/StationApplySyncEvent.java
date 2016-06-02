package com.taobao.cun.auge.event;

import java.io.Serializable;

import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyParamTypeEnum;

/**
 * 同步到station_apply老模型
 * 
 * @author linjianke
 *
 */
public class StationApplySyncEvent implements Serializable {

	private static final long serialVersionUID = 4078235655790992730L;

	private SyncStationApplyEnum syncType;

	private SyncStationApplyParamTypeEnum paramType = SyncStationApplyParamTypeEnum.PARTNER_INSTANCE_ID;

	private Long objectId;

	public StationApplySyncEvent(SyncStationApplyEnum syncType, Long objectId) {
		this.syncType = syncType;
		this.objectId = objectId;
	}

	public StationApplySyncEvent(SyncStationApplyEnum syncType, SyncStationApplyParamTypeEnum paramType, Long objectId) {
		this.syncType = syncType;
		this.paramType = paramType;
		this.objectId = objectId;
	}

	public SyncStationApplyEnum getSyncType() {
		return syncType;
	}

	public void setSyncType(SyncStationApplyEnum syncType) {
		this.syncType = syncType;
	}

	public SyncStationApplyParamTypeEnum getParamType() {
		return paramType;
	}

	public void setParamType(SyncStationApplyParamTypeEnum paramType) {
		this.paramType = paramType;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

}
