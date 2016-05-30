package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 定时任务调用服务
 * @author quanzhu.wangqz
 *
 */
public interface PartnerInstanceScheduleService {

	/**
	 * 获得待开业数据
	 * @param fetchNum
	 * @return
	 * @throws AugeServiceException
	 */
	public List<Long> getWaitOpenStationList(int fetchNum) throws AugeServiceException;
	
	
	/**
	 * 定时开业
	 * @param instanceId
	 * @return
	 * @throws AugeServiceException
	 */
	public Boolean openStation(Long instanceId) throws AugeServiceException;
}
