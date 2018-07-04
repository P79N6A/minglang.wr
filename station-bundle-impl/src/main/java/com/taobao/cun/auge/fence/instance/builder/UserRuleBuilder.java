package com.taobao.cun.auge.fence.instance.builder;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.instance.result.UserRuleBuilderResult;
import com.taobao.cun.auge.fence.instance.rule.UserFenceRule;

@Component
public class UserRuleBuilder implements RuleBuilder<UserFenceRule, UserRuleBuilderResult> {

	@Override
	public UserRuleBuilderResult build(Station station, UserFenceRule fenceRule) {
		return null;
	}
	

}
