package com.taobao.cun.auge.level.enterrule.data;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.level.dto.TownLevelDto;

@Component("emptyRuleDataBuilder")
public class EmptyRuleDataBuilder implements RuleDataBuilder {

	@Override
	public Map<String, Object> build(TownLevelDto townLevelDto) {
		return Maps.newHashMap();
	}

}
