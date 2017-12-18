package com.taobao.cun.auge.station.service;

import java.util.List;

/**
 * 村点校验服务
 */
public interface StationCheckService {

	/**
	 * 校验村点撤点，是否满足条件
	 */
	public void checkShutdownApply(Long stationId);
	
	
	/**
	 * 和菜鸟全量数据对账
	 * @param staitonId
	 */
	public void checkAllWithCainiao(List<Long> stationIds);
}
