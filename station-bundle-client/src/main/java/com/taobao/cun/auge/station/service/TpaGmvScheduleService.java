package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.PageQuery;
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
	 * @param page
	 * @return
	 * @throws AugeServiceException
	 */
	public PageDto<DwiCtStationTpaIncomeMDto> getWaitAddChildNumStationList(PageQuery page) throws AugeServiceException;

	/**
	 * 合伙人连续两个月GMV排名，所在县前20%，其淘帮手名额添加2个
	 * 
	 * @param stationId
	 * @return
	 */
	public Boolean addChildNumByGmv(DwiCtStationTpaIncomeMDto incomeDto);
	
	/**
	 * 淘帮手前两个自然月（满两个自然月），每月含佣父订单<10笔且含佣GMV<1000元
	 * 
	 * @param page
	 * @return
	 * @throws AugeServiceException
	 */
	public PageDto<DwiCtStationTpaIncomeMDto> getWaitClosingTpaList(PageQuery page) throws AugeServiceException;
}
