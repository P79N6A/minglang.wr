package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;

/**
 * 镇上的优品服务站(V4.0)
 * 
 * @author chengyu.zhoucy
 *
 */
public class TownYoupinInitingStationFetcher extends AbstractInitingStationFetcher {

	@Override
	protected List<Long> getTemplateIds() {
		return Lists.newArrayList(
			fenceInitTemplateConfig.getTemplateIdSellYoupinTown()
		);
	}

	@Override
	protected List<Station> getFenceInitingStations(Long templateId) {
		FenceInitingStationQueryCondition condition = new FenceInitingStationQueryCondition();
		condition.setStationLocations(Lists.newArrayList("town"));
		condition.setStationTypes(Lists.newArrayList("TP_v4"));
		condition.setTemplateId(templateId);
		return stationBO.getFenceInitingStations(condition);
	}

}
