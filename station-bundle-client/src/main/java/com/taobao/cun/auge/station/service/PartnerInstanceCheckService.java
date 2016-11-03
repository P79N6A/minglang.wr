package com.taobao.cun.auge.station.service;

/**
 * 合伙人实例，校验服务
 */
public interface PartnerInstanceCheckService {
	
	/**
	 * 校验合伙人实例停业申请，是否满足条件
	 */
	public void checkCloseApply(Long instanceId);
	
	
	/**
	 * 校验合伙人实例停业申请，是否满足条件
	 */
	public void checkQuitApply(Long instanceId);

}
