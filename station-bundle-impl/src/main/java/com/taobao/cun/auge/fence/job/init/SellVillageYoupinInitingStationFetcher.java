package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;

/**
 * 售卖围栏 - 村类型服务站优品售卖围栏
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class SellVillageYoupinInitingStationFetcher extends AbstractInitingStationFetcher {
	@Override
	protected List<Station> getFenceInitingStations() {
		FenceInitingStationQueryCondition condition = new FenceInitingStationQueryCondition();
		condition.setStationLocations(Lists.newArrayList("village"));
		condition.setStationTypes(Lists.newArrayList("TP_v4"));
		condition.setTemplateId(getTemplateId());
		return stationBO.getFenceInitingStations(condition);
	}
	
	@Override
	protected Long getTemplateId() {
		return fenceInitTemplateConfig.getTemplateIdSellYoupinVillage();
	}

}
