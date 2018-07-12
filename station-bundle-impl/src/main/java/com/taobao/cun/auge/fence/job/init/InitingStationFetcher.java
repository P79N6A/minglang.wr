package com.taobao.cun.auge.fence.job.init;

/**
 * 获取待初始化的站点
 * 
 * @author chengyu.zhoucy
 *
 */
public interface InitingStationFetcher {
	/**
	 * 获取待初始化的站点
	 * @return
	 */
	InitingStation getInitingStations();
}
