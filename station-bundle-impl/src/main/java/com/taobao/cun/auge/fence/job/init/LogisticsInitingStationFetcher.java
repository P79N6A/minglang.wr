package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;

/**
 * 物流围栏 - 服务站推荐围栏
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class LogisticsInitingStationFetcher extends AbstractInitingStationFetcher {
	@Value("${fence.templateid.logistics}")
	private Long templateId;
	@Override
	protected List<Station> getFenceInitingStations() {
		FenceInitingStationQueryCondition condition = new FenceInitingStationQueryCondition();
		condition.setStationTypes(Lists.newArrayList("TP"));
		condition.setTemplateId(getTemplateId());
		return stationBO.getFenceInitingStations(condition);
	}
	
	@Override
	protected Long getTemplateId() {
		return templateId;
	}

}
