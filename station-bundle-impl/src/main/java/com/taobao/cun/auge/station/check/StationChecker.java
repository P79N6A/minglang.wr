package com.taobao.cun.auge.station.check;

public interface StationChecker {

	/*
	 * check撤点的前提条件是否满足
	 */
	void checkShutdownApply(Long stationId);

}
