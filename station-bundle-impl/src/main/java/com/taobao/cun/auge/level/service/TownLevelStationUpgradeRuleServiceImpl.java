package com.taobao.cun.auge.level.service;

import javax.annotation.Resource;

import com.taobao.cun.auge.level.bo.TownLevelStationUpgradeRuleBo;
import com.taobao.cun.auge.level.dto.TownLevelStationSetting;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= TownLevelStationUpgradeRuleService.class)
public class TownLevelStationUpgradeRuleServiceImpl implements TownLevelStationUpgradeRuleService {
	@Resource
	private TownLevelStationUpgradeRuleBo townLevelStationUpgradeRuleBo;
	
	@Override
	public TownLevelStationSetting getTownLevelStationRule(long stationId) {
		return townLevelStationUpgradeRuleBo.getTownLevelStationRule(stationId);
	}

}
