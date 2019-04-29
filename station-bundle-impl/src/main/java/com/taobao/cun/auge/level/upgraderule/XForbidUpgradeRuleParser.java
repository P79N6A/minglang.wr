package com.taobao.cun.auge.level.upgraderule;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.settingrule.rule.RuleResult;

@Component("xforbidUpgradeRuleParser")
public class XForbidUpgradeRuleParser implements UpgradeRuleParser {

	@Override
	public List<RuleResult> doParse(TownLevelDto townLevelDto, Long stationId) {
		return Lists.newArrayList(new RuleResult("CLOSE", "该镇为超标镇，暂不开天猫优品电器体验店"));
	}

}
