package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.StationBO;

/**
 * 站点初始化抽像实现
 * 
 * @author chengyu.zhoucy
 *
 */
public abstract class AbstractInitingStationFetcher implements InitingStationFetcher {
	@Resource
	protected StationBO stationBO;
	@Resource
	protected FenceInitTemplateConfig fenceInitTemplateConfig;
	
	@Override
	public List<InitingStation> getInitingStations() {
		List<InitingStation> initingStations = Lists.newArrayList();
		
		for(Long templateId : getTemplateIds()) {
			initingStations.add(new InitingStation(getFenceInitingStations(templateId), templateId));
		}
		
		return initingStations;
	}
	/**
	 * 获取模板ID列表
	 * @return
	 */
	protected abstract List<Long> getTemplateIds();
	
	/**
	 * 查询站点
	 * @param templateId
	 * @return
	 */
	protected abstract List<Station> getFenceInitingStations(Long templateId);
}
