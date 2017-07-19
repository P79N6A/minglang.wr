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
	 */
	public StationApply addStationApply(Long partnerInstanceId);

	/**
	 * 将数据同步修改到老模型
	 * 
	 * @param partnerInstanceId
	 */
	public void updateStationApply(Long partnerInstanceId, SyncStationApplyEnum updateType);

	/**
	 * 删除
	 * @param instanceId
	 */
	public void deleteStationApply(Long instanceId);
	
	/**
	 * 验证station_apply和partner_station_rel总数
	 */
	public void checkTotalStationApplySize();

}
