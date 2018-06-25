package com.taobao.cun.auge.station.service;

/**
 *  黑名单镇判断
 * @author chengyu.zhoucy
 *
 */
public interface TownBlacknameService {
	/**
	 * 判断某个县下，某个镇是否属于黑名单
	 * @param countyName
	 * @param townName
	 * @return
	 */
	boolean isBlackname(String countyName, String townName);
	
	/**
	 * 记录黑名单
	 * @param partnerApplyId
	 */
	void recordPartnerApplyBlackname(Long partnerApplyId);
}
