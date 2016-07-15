package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;
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
	public List<StationDecorateDto> getStationDecorateListForSchedule(int fetchNum) throws AugeServiceException;
	
	
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
	 * 根据主键更新装修记录表状态
	 * @param id
	 * @param statusEnum
	 * @param operatorDto
	 * @throws AugeServiceException
	 */
	public void updateStatus(Long id,StationDecorateStatusEnum statusEnum,OperatorDto operatorDto) throws AugeServiceException;
}
