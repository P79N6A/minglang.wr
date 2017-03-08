package com.taobao.cun.auge.station.service;

/**
 * 村点校验服务
 */
public interface StationCheckService {

	/**
	 * 校验村点撤点，是否满足条件
	 */
	public void checkShutdownApply(Long stationId);
}
