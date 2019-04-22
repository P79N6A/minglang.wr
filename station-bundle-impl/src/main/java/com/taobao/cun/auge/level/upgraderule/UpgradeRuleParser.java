package com.taobao.cun.auge.level.upgraderule;

import java.util.List;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationSetting;
import com.taobao.cun.auge.level.enterrule.setting.rule.RuleResult;

public interface UpgradeRuleParser {
	List<RuleResult> doParse(TownLevelDto townLevelDto, Long stationId);
	
	default List<TownLevelStationSetting> parse(TownLevelDto townLevelDto, Long stationId) {
		List<RuleResult> ruleResults = doParse(townLevelDto, stationId);
		List<TownLevelStationSetting> townLevelStationSettings = Lists.newArrayList();
		for(RuleResult ruleResult : ruleResults) {
			TownLevelStationSetting townLevelStationSetting = new TownLevelStationSetting();
			townLevelStationSetting.setAreaCode(townLevelDto.getTownCode());
			townLevelStationSetting.setLevel(townLevelDto.getLevel());
			townLevelStationSetting.setStationTypeCode(ruleResult.getCode());
			townLevelStationSetting.setStationTypeDesc(ruleResult.getMessage());
			townLevelStationSettings.add(townLevelStationSetting);
		}
		return townLevelStationSettings;
	}
}
