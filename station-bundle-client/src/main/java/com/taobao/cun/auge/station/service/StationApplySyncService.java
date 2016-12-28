package com.taobao.cun.auge.station.service;

public interface StationApplySyncService {
	/**
	 * 从partner_station_rel回写数据至station_apply以及附件和固点协议
	 * 
	 * @param partnerInstanceId
	 */
	public void syncToStationApply(Long partnerInstanceId);

	/**
	 * 检验station_apply和partner_station_rel总数
	 */
	public void checkTotalStationApplySize();

}
