package com.taobao.cun.auge.fence.instance.builder;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.instance.result.RuleBuilderResult;
import com.taobao.cun.auge.fence.instance.rule.FenceRule;

public interface RuleBuilder<R extends FenceRule, S extends RuleBuilderResult> {
	
	S build(Station station, R fenceRule);
}
