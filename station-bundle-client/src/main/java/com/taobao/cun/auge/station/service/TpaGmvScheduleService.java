package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.DwiCtStationTpaIncomeMDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 定时任务调用服务
 *
 */
public interface TpaGmvScheduleService {

	/**
	 * 获得合伙人，其名下淘帮手连续两个月GMV排名，所在县前20%
	 * 
	 * @param fetchNum
	 * @return
	 * @throws AugeServiceException
	 */
	public List<DwiCtStationTpaIncomeMDto> getWaitAddChildNumStationList(int fetchNum) throws AugeServiceException;

	/**
	 * 合伙人连续两个月GMV排名，所在县前20%，其淘帮手名额添加2个
	 * 
	 * @param stationId
	 * @return
	 */
	public Boolean addChildNumByGmv(DwiCtStationTpaIncomeMDto incomeDto);
}