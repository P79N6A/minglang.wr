package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;

/**
 * 淘帮手
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TPAStationInitingStationFetcher extends AbstractInitingStationFetcher {

	@Override
	protected List<Long> getTemplateIds() {
		return Lists.newArrayList(10L);
	}

	@Override
	protected List<Station> getFenceInitingStations(Long templateId) {
		FenceInitingStationQueryCondition condition = new FenceInitingStationQueryCondition();
		condition.setStationTypes(Lists.newArrayList("TPA"));
		condition.setTemplateId(templateId);
		return stationBO.getFenceInitingStations(condition);
	}

}
