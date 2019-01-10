package com.taobao.cun.auge.level.enterrule.setting.rule;

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
	RuleResult doParse(TownLevelDto townLevelDto, TownLevelStationRuleDto townLevelStationRuleDto);
	
	default TownLevelStationSetting parse(TownLevelDto townLevelDto) {
		RuleResult ruleResult = doParse(townLevelDto, townLevelDto.getTownLevelStationRuleDto());
		TownLevelStationSetting townLevelStationSetting = new TownLevelStationSetting();
		townLevelStationSetting.setAreaCode(townLevelDto.getTownCode());
		townLevelStationSetting.setLevel(townLevelDto.getLevel());
		townLevelStationSetting.setStationTypeCode(ruleResult.getCode());
		townLevelStationSetting.setStationTypeDesc(ruleResult.getMessage());
		return townLevelStationSetting;
	}
}
