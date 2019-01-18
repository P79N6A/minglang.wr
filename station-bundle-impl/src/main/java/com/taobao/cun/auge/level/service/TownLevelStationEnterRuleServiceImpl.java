package com.taobao.cun.auge.level.service;

import javax.annotation.Resource;

import com.taobao.cun.auge.level.bo.TownLevelStationEnterRuleBo;
import com.taobao.cun.auge.level.dto.TownLevelStationSetting;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= TownLevelStationEnterRuleService.class)
public class TownLevelStationEnterRuleServiceImpl implements TownLevelStationEnterRuleService {
	@Resource
	private TownLevelStationEnterRuleBo townLevelStationEnterRuleBo;
	
	@Override
	public TownLevelStationSetting getTownLevelStationRule(String townCode) {
		return townLevelStationEnterRuleBo.getTownLevelStationRule(townCode);
	}

}
