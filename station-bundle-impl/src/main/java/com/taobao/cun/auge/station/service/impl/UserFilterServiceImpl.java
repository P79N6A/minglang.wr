package com.taobao.cun.auge.station.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.UserFilterRule;
import com.taobao.cun.auge.dal.domain.UserFilterRuleExample;
import com.taobao.cun.auge.dal.mapper.UserFilterRuleMapper;
import com.taobao.cun.auge.station.service.UserFilterService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= UserFilterService.class)
public class UserFilterServiceImpl implements UserFilterService {
	@Resource
	private UserFilterRuleMapper userFilterRuleMapper;
	
	private static final Map<String,Rule> RULES = Maps.newHashMap();
	
	static {
		RULES.put("trueRule", new TrueRule());
	}
	
	@Override
	public boolean isMatch(String bizType, String userId) {
		UserFilterRule userFilterRule = getUserFilterRule(bizType, userId);
		if(userFilterRule == null) {
			return false;
		}
		return RULES.get(userFilterRule.getFilterRule()).isMatch(userId);
	}

	private UserFilterRule getUserFilterRule(String bizType, String userId) {
		UserFilterRuleExample example = new UserFilterRuleExample();
		example.createCriteria().andBizTypeEqualTo(bizType).andUserIdEqualTo(userId);
		List<UserFilterRule> userFilterRules = userFilterRuleMapper.selectByExample(example);
		if(CollectionUtils.isNotEmpty(userFilterRules)) {
			return userFilterRules.get(0);
		}else {
			return null;
		}
	}
}

interface Rule{
	boolean isMatch(String userId);
}

class TrueRule implements Rule{

	public boolean isMatch(String userId) {
		return true;
	}
}
