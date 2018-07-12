package com.taobao.cun.auge.fence.job;

import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.dto.job.StationInitFenceInstanceJob;
import com.taobao.cun.auge.fence.job.init.InitingStation;
import com.taobao.cun.auge.fence.job.init.InitingStationFetcher;

/**
 * 初始化栏实例的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationInitFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationInitFenceInstanceJob> implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	
	@Override
	protected int doExecute(StationInitFenceInstanceJob fenceInstanceJob) {
		int instanceNum = 0;
		Map<String, InitingStationFetcher> map = applicationContext.getBeansOfType(InitingStationFetcher.class);
		for(InitingStationFetcher initingStationFetcher : map.values()) {
			InitingStation initingStation = initingStationFetcher.getInitingStations();
			if(CollectionUtils.isNotEmpty(initingStation.getStations())) {
				for(Station station : initingStation.getStations()) {
					buildFenceEntity(station.getId(), initingStation.getTemplateId());
				}
				
				instanceNum += initingStation.getStations().size();
			}
		}
		return instanceNum;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
