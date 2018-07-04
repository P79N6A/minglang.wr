package com.taobao.cun.auge.fence.instance.builder;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.instance.result.RangeRuleBuilderResult;
import com.taobao.cun.auge.fence.instance.rule.RangeFenceRule;

@Component
public class RangeRuleBuilder implements RuleBuilder<RangeFenceRule, RangeRuleBuilderResult> {

	@Override
	public RangeRuleBuilderResult build(Station station, RangeFenceRule fenceRule) {
		return null;
	}
	

}
