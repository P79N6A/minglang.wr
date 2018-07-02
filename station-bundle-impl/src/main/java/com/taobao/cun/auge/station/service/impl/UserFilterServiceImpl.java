package com.taobao.cun.auge.station.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.UserFilterRule;
import com.taobao.cun.auge.dal.domain.UserFilterRuleExample;
import com.taobao.cun.auge.dal.mapper.UserFilterRuleMapper;
import com.taobao.cun.auge.station.dto.UserFilterRuleDto;
import com.taobao.cun.auge.station.service.UserFilterService;
import com.taobao.cun.common.util.BeanCopy;
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
		List<UserFilterRule> userFilterRules = getUserFilterRule(bizType, userId);
		if(userFilterRules == null) {
			return false;
		}
		
		for(UserFilterRule userFilterRule : userFilterRules) {
			Rule rule = RULES.get(userFilterRule.getFilterRule());
			if(rule != null && rule.isMatch(userId)) {
				return true;
			}
		}
		
		return false;
	}

	private List<UserFilterRule> getUserFilterRule(String bizType, String userId) {
		UserFilterRuleExample example = new UserFilterRuleExample();
		example.createCriteria().andBizTypeEqualTo(bizType).andUserIdEqualTo(userId);
		return userFilterRuleMapper.selectByExample(example);
	}

	@Override
	public void addUserFilterRule(UserFilterRuleDto userFilterRuleDto) {
		if(CollectionUtils.isNotEmpty(getUserFilterRule(userFilterRuleDto.getBizType(), userFilterRuleDto.getUserId()))) {
			return;
		}
		UserFilterRule record = BeanCopy.copy(UserFilterRule.class, userFilterRuleDto);
		record.setGmtCreate(new Date());
		record.setGmtModified(new Date());
		userFilterRuleMapper.insert(record);
	}

	@Override
	public List<UserFilterRuleDto> getUserFilterRules(String bizType, String userId) {
		List<UserFilterRule> userFilterRules = getUserFilterRule(bizType, userId);
		List<UserFilterRuleDto> result = Lists.newArrayList();
		
		for(UserFilterRule userFilterRule : userFilterRules) {
			result.add(BeanCopy.copy(UserFilterRuleDto.class, userFilterRule));
		}
		return result;
	}

	@Override
	public void deleteUserFilterRule(Long id) {
		userFilterRuleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void deleteUserFilterRules(String bizType, String userId) {
		UserFilterRuleExample example = new UserFilterRuleExample();
		example.createCriteria().andBizTypeEqualTo(bizType).andUserIdEqualTo(userId);
		userFilterRuleMapper.deleteByExample(example);	
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
