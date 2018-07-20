package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;

/**
 * 镇上的体验店(门店)
 * 
 * @author chengyu.zhoucy
 *
 */
public class TownStoreInitingStationFetcher extends AbstractInitingStationFetcher {

	@Override
	protected List<Long> getTemplateIds() {
		return Lists.newArrayList(
			fenceInitTemplateConfig.getTemplateIdSellStoreTown()
		);
	}

	@Override
	protected List<Station> getFenceInitingStations(Long templateId) {
		FenceInitingStationQueryCondition condition = new FenceInitingStationQueryCondition();
		condition.setStationLocations(Lists.newArrayList("town"));
		condition.setStationTypes(Lists.newArrayList("TPS"));
		condition.setTemplateId(templateId);
		return stationBO.getFenceInitingStations(condition);
	}

}
