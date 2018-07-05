package com.taobao.cun.auge.fence.instance.builder;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.instance.rule.UserFenceRule;

/**
 * 构建用户规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class UserRuleBuilder implements RuleBuilder<UserFenceRule> {

	@Override
	public String build(Station station, UserFenceRule fenceRule) {
		return fenceRule.getUserType();
	}
	

}
