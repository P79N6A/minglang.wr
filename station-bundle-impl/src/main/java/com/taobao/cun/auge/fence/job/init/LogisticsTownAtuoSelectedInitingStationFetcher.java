package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;

/**
 * 物流围栏 - 镇级服务站默认勾选围栏
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class LogisticsTownAtuoSelectedInitingStationFetcher extends AbstractInitingStationFetcher {
	@Override
	protected List<Station> getFenceInitingStations() {
		FenceInitingStationQueryCondition condition = new FenceInitingStationQueryCondition();
		condition.setStationLocations(Lists.newArrayList("town"));
		condition.setStationTypes(Lists.newArrayList("TP"));
		condition.setTemplateId(getTemplateId());
		return stationBO.getFenceInitingStations(condition);
	}
	
	@Override
	protected Long getTemplateId() {
		return fenceInitTemplateConfig.getTemplateIdLogisticsTownAtuoselected();
	}

}
