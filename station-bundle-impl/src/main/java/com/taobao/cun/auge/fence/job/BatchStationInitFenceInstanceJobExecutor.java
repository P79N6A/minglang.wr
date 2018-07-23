package com.taobao.cun.auge.fence.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.dto.job.BatchStationInitFenceInstanceJob;
import com.taobao.cun.auge.fence.job.init.InitingStation;
import com.taobao.cun.auge.fence.job.init.InitingStationFetcher;

/**
 * 批量初始化站点围栏实例
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class BatchStationInitFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<BatchStationInitFenceInstanceJob> implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	
	@Override
	protected int doExecute(BatchStationInitFenceInstanceJob fenceInstanceJob) {
		int instanceNum = 0;
		Map<String, InitingStationFetcher> map = applicationContext.getBeansOfType(InitingStationFetcher.class);
		for(InitingStationFetcher initingStationFetcher : map.values()) {
			for(InitingStation initingStation : initingStationFetcher.getInitingStations()) {
				instanceNum += init(initingStation);
			}
		}
		return instanceNum;
	}
	
	private int init(InitingStation initingStation) {
		if(CollectionUtils.isNotEmpty(initingStation.getStations())) {
			for(Station station : initingStation.getStations()) {
				newFenceEntity(station.getId(), initingStation.getTemplateId());
			}
			return initingStation.getStations().size();
		}else {
			return 0;
		}
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
