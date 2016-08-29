package com.taobao.cun.auge.station.bo;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 服务站装修记录服务接口
 * @author quanzhu.wangqz
 *
 */
public interface StationDecorateBO {
	
	/**
	 * 新增装修记录
	 * @param stationDecorateDto
	 * @return
	 * @throws AugeServiceException
	 */
	public StationDecorate  addStationDecorate(StationDecorateDto stationDecorateDto)  throws AugeServiceException;
	
	/**
	 * 获得装修记录列表,提供给定时任务使用
	 * @param sdCondition
	 * @return
	 * @throws AugeServiceException
	 */
	public List<StationDecorateDto> getStationDecorateListForSchedule(int pageNum,int pageSize) throws AugeServiceException;
	
	/**
	 * 获得装修记录总数,提供给定时任务使用
	 * @param sdCondition
	 * @return
	 * @throws AugeServiceException
	 */
	public int getStationDecorateListCountForSchedule() throws AugeServiceException;
	
	/**
	 * 定时任务调用同步淘宝订单
	 * @param stationDecorateDto
	 * @throws AugeServiceException
	 */
	public void syncStationDecorateFromTaobao(StationDecorateDto stationDecorateDto)throws AugeServiceException;
	
	
	/**
	 * 修改装修记录
	 * @param stationDecorateDto
	 * @throws AugeServiceException
	 */
	public void  updateStationDecorate(StationDecorateDto stationDecorateDto) throws AugeServiceException;
	
	
	/**
	 * 根据stationid查询装修记录
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public StationDecorateDto getStationDecorateDtoByStationId(Long stationId) throws AugeServiceException;
	
	/**
	 * 根据stationid查询装修记录
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public StationDecorate getStationDecorateByStationId(Long stationId) throws AugeServiceException;
	
	/**
	 * 根据主键查询服务站装修记录
	 * @param id
	 * @return
	 * @throws AugeServiceException
	 */
	public StationDecorate getStationDecorateById(Long id) throws AugeServiceException;
	
	/**
	 * 根据staitonId获得状态
	 * @param stationIds
	 * @return
	 * @throws AugeServiceException
	 */
	public Map<Long, StationDecorateStatusEnum> getStatusByStationId(List<Long> stationIds) throws AugeServiceException;
	
	/**
	 * 进入装修中判断装修记录是否可以完成
	 */
	public boolean handleAcessDecorating(Long stationId);
	/**
	 * 合伙人确认进入装修中
	 * @param id
	 */
 	public void confirmAcessDecorating(Long id);

 	/**
 	 * 作废装修记录
 	 */
 	public void invalidStationDecorate(Long stationId);
}
