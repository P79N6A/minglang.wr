package com.taobao.cun.auge.station.sync;

import com.taobao.cun.auge.dal.domain.StationApply;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface StationApplySyncBO {

	/**
	 * 将数据同步新增到老模型
	 * 
	 * @param partnerInstanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public StationApply addStationApply(Long partnerInstanceId) throws AugeServiceException;

	/**
	 * 将数据同步修改到老模型
	 * 
	 * @param partnerInstanceId
	 * @throws AugeServiceException
	 */
	public void updateStationApply(Long partnerInstanceId, SyncStationApplyEnum updateType) throws AugeServiceException;

}
