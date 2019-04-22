package com.taobao.cun.auge.cuncounty.service;

/**
 * 县服务中心待开业审批流程
 * @author chengyu.zhoucy
 *
 */
public interface CuntaoCountyWaitOpenProcessService {
	/**
	 * 发起流程
	 * @param countyId
	 */
	void start(Long countyId, String operator);
	/**
	 * 同意
	 * @param countyId
	 */
	void agree(Long countyId);
	
	/**
	 * 拒绝
	 * @param countyId
	 */
	void deny(Long countyId);
}
