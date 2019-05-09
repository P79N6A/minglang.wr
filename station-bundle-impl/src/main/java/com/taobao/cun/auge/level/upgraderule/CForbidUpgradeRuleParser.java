package com.taobao.cun.auge.level.upgraderule;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.settingrule.rule.RuleResult;

@Component("cforbidUpgradeRuleParser")
public class CForbidUpgradeRuleParser implements UpgradeRuleParser {

	@Override
	public List<RuleResult> doParse(TownLevelDto townLevelDto, Long stationId) {
		return Lists.newArrayList(new RuleResult("CLOSE", "该镇为C镇，暂不开点"));
	}

}
