package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;

/**
 * 收费围栏 - 村级服务站默认收费围栏
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class FeeVillageInitingStationFetcher extends AbstractInitingStationFetcher {
	@Value("${fence.templateid.fee.village}")
	private Long templateId;
	@Override
	protected List<Station> getFenceInitingStations() {
		FenceInitingStationQueryCondition condition = new FenceInitingStationQueryCondition();
		condition.setStationLocations(Lists.newArrayList("village"));
		condition.setStationTypes(Lists.newArrayList("TP"));
		condition.setTemplateId(getTemplateId());
		return stationBO.getFenceInitingStations(condition);
	}
	@Override
	protected Long getTemplateId() {
		return templateId;
	}
}
