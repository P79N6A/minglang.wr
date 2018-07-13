package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.StationBO;

/**
 * 站点初始化抽闲实现
 * 
 * @author chengyu.zhoucy
 *
 */
public abstract class AbstractInitingStationFetcher implements InitingStationFetcher {
	@Resource
	protected StationBO stationBO;
	
	@Override
	public InitingStation getInitingStations() {
		List<Station> stations = getFenceInitingStations();
		return new InitingStation(stations, getTemplateId());
	}

	/**
	 * 获取默认模板ID
	 * @return
	 */
	protected abstract Long getTemplateId();

	/**
	 * 查询需要初始化的站点
	 * @return
	 */
	protected abstract List<Station> getFenceInitingStations();
}
