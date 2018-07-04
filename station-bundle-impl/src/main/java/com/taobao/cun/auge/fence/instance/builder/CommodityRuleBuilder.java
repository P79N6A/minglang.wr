package com.taobao.cun.auge.fence.instance.builder;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.instance.result.CommodityRuleBuilderResult;
import com.taobao.cun.auge.fence.instance.rule.CommodityFenceRule;

@Component
public class CommodityRuleBuilder implements RuleBuilder<CommodityFenceRule, CommodityRuleBuilderResult> {

	@Override
	public CommodityRuleBuilderResult build(Station station, CommodityFenceRule fenceRule) {
		return null;
	}

}
