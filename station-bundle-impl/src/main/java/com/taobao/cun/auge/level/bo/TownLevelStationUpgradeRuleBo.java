package com.taobao.cun.auge.level.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.service.StationQueryService;

@Component
public class TownLevelStationUpgradeRuleBo {
	@Resource
	private StationQueryService stationQueryService;
	@Resource
	private TownLevelStationEnterRuleBo townLevelStationEnterRuleBo;
	
	public TownLevelStationRuleDto getTownLevelStationRule(long stationId) {
		StationDto stationDto = stationQueryService.getStation(stationId);
		return townLevelStationEnterRuleBo.getTownLevelStationRule(stationDto.getAddress().getTown());
	}
}
