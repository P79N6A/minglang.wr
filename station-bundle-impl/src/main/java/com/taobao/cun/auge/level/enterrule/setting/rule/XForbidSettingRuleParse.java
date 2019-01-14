package com.taobao.cun.auge.level.enterrule.setting.rule;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;

/**
 * 超标镇禁入规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("xforbidSettingRuleParse")
public class XForbidSettingRuleParse implements SettingRuleParse {

	@Override
	public RuleResult doParse(TownLevelDto townLevelDto, TownLevelStationRuleDto townLevelStationRuleDto) {
		return new RuleResult("CLOSE", "该镇为超标镇，暂不开天猫优品电器体验店");
	}

}
