package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.ShutDownStationApply;

public interface ShutDownStationApplyBO {

	/**
	 * 保存撤点申请单
	 * 
	 * @param quitStationApply
	 * @param operator
	 * @return
	 */
	public void saveShutDownStationApply(ShutDownStationApply shutDownStationApply, String operator);

	/**
	 * 根据村点id，查询撤点申请单
	 * 
	 * @param stationId
	 * @return
	 */
	public ShutDownStationApply findShutDownStationApply(Long stationId);

	/**
	 * 根据村点id，删除撤点申请单
	 * 
	 * @param stationId
	 */
	public void deleteShutDownStationApply(Long instanceId, String operator);
	
}
