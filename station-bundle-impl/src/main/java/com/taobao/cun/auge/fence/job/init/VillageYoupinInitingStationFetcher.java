package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;

/**
 * 村上的优品服务站(V4.0)
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class VillageYoupinInitingStationFetcher extends AbstractInitingStationFetcher {

	@Override
	protected List<Long> getTemplateIds() {
		return Lists.newArrayList(
			fenceInitTemplateConfig.getTemplateIdSellYoupinVillage()
		);
	}

	@Override
	protected List<Station> getFenceInitingStations(Long templateId) {
		FenceInitingStationQueryCondition condition = new FenceInitingStationQueryCondition();
		condition.setStationLocations(Lists.newArrayList("village"));
		condition.setStationTypes(Lists.newArrayList("TP_v4"));
		condition.setTemplateId(templateId);
		return stationBO.getFenceInitingStations(condition);
	}

}
