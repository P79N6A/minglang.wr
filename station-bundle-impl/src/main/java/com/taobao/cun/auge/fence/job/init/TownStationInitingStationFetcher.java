package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import com.taobao.cun.auge.station.enums.StationStatusEnum;
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
			fenceInitTemplateConfig.getLogisticsDefault(),
			fenceInitTemplateConfig.getLogisticsTown(),
			fenceInitTemplateConfig.getLogisticsTownDefault()
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
		return StationStatusEnum.SERVICING.getCode().equals(station.getStatus()) && caiNiaoService.checkCainiaoStationIsOperating(station.getId()) && caiNiaoService.checkCainiaoCountyIsOperating(station.getId());
	}

}
