package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.UserFilterRuleDto;

/**
 * 用户过滤
 * 
 * @author chengyu.zhoucy
 *
 */
public interface UserFilterService {
	/**
	 * 用户是否匹配配置的规则
	 * 
	 * @param bizType
	 * @param userId
	 * @return
	 */
	boolean isMatch(String bizType, String userId);
	
	/**
	 * 添加一条过滤规则
	 * @param userFilterDto
	 */
	void addUserFilterRule(UserFilterRuleDto userFilterRuleDto);
	
	/**
	 * 查询过滤规则
	 * @param bizType
	 * @param userId
	 * @return
	 */
	List<UserFilterRuleDto> getUserFilterRules(String bizType, String userId);
	
	/**
	 * 删除一条过滤规则
	 * @param id
	 */
	void deleteUserFilterRule(Long id);
	
	/**
	 * 删除过滤规则
	 * @param id
	 */
	void deleteUserFilterRules(String bizType, String userId);
}
