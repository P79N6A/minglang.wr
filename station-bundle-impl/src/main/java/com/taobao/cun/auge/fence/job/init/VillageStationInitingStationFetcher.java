package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;

/**
 * 村上的服务站（普通服务站、优品服务站）
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class VillageStationInitingStationFetcher extends AbstractInitingStationFetcher {

	@Override
	protected List<Long> getTemplateIds() {
		return Lists.newArrayList(
			fenceInitTemplateConfig.getTemplateIdFeeVillage()/*,
			fenceInitTemplateConfig.getTemplateIdLogistics(),
			fenceInitTemplateConfig.getTemplateIdLogisticsVillageAtuoselected()*/
		);
	}

	@Override
	protected List<Station> getFenceInitingStations(Long templateId) {
		FenceInitingStationQueryCondition condition = new FenceInitingStationQueryCondition();
		condition.setStationLocations(Lists.newArrayList("village"));
		condition.setStationTypes(Lists.newArrayList("TP"));
		condition.setTemplateId(templateId);
		return stationBO.getFenceInitingStations(condition);
	}

}
