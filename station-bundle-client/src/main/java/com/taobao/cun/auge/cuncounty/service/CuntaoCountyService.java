package com.taobao.cun.auge.cuncounty.service;

/**
 * 县服务中心
 * 
 * @author chengyu.zhoucy
 *
 */
public interface CuntaoCountyService {
	/**
	 * 县点申请开业
	 * 
	 * @param countyId
	 */
	void applyOpen(Long countyId, String operator);
}
