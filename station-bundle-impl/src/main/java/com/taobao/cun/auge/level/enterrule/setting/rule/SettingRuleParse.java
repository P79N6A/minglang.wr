package com.taobao.cun.auge.level.enterrule.setting.rule;

import java.util.List;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;
import com.taobao.cun.auge.level.dto.TownLevelStationSetting;

/**
 * 入驻规则解析
 * 
 * @author chengyu.zhoucy
 *
 */
public interface SettingRuleParse {
	List<RuleResult> doParse(TownLevelDto townLevelDto, TownLevelStationRuleDto townLevelStationRuleDto);
	
	default List<TownLevelStationSetting> parse(TownLevelDto townLevelDto) {
		List<RuleResult> ruleResults = doParse(townLevelDto, townLevelDto.getTownLevelStationRuleDto());
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
