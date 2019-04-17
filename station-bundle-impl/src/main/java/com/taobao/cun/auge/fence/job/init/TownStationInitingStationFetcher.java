package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;

/**
 * 镇上的服务站（普通服务站、优品服务站）
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TownStationInitingStationFetcher extends AbstractInitingStationFetcher {

	@Override
	protected List<Long> getTemplateIds() {
		return Lists.newArrayList(
			fenceInitTemplateConfig.getTemplateIdFeeTown(),
			fenceInitTemplateConfig.getTemplateIdSellTown(),
			fenceInitTemplateConfig.getTemplateIdSellClothing(),
			fenceInitTemplateConfig.getTemplateIdSellAppliances(),
			fenceInitTemplateConfig.getTemplateIdLogisticsDefault(),
			fenceInitTemplateConfig.getTemplateIdLogisticsTown(),
			fenceInitTemplateConfig.getTemplateIdLogisticsTownDefault()
		);
	}

	@Override
	protected List<Station> getFenceInitingStations(Long templateId) {
		FenceInitingStationQueryCondition condition = new FenceInitingStationQueryCondition();
		condition.setStationLocations(Lists.newArrayList("town"));
		condition.setStationTypes(Lists.newArrayList("TP"));
		condition.setTemplateId(templateId);
		return stationBO.getFenceInitingStations(condition);
	}

	@Override
	protected boolean matchSellCondition(Station station) {
		return true;
	}

	@Override
	protected boolean matchChargeCondition(Station station) {
		return caiNiaoService.checkCainiaoStationIsOperating(station.getId()) && caiNiaoService.checkCainiaoCountyIsOperating(station.getId());
	}

	@Override
	protected boolean matchLogisticsCondition(Station station) {
		return caiNiaoService.checkCainiaoStationIsOperating(station.getId()) && caiNiaoService.checkCainiaoCountyIsOperating(station.getId());
	}

}
