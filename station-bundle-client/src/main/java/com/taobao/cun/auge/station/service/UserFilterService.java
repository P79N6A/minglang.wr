package com.taobao.cun.auge.station.service;

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
}
