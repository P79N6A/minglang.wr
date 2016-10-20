package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 服务站查询接口
 */
public interface StationQueryService {
	/**
	 * 根据主键查询
	 * 
	 * @param stationCondition
	 * @return
	 * @throws AugeServiceException
	 */
	public StationDto queryStationInfo(StationCondition stationCondition) throws AugeServiceException;

	/**
	 * 根据station id查询station
	 * 
	 * @param stationIds
	 * @return
	 * @throws AugeServiceException
	 */
	public List<StationDto> queryStations(List<Long> stationIds) throws AugeServiceException;

	/**
	 * 根据name、orgIdPath、stationStatusEnum查询station
	 * 
	 * @param stationCondition
	 * @return
	 * @throws AugeServiceException
	 */
	public List<StationDto> queryStationsByName(StationCondition stationCondition) throws AugeServiceException;

	/**
	 * 查询撤点申请单
	 * 
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public ShutDownStationApplyDto findShutDownStationApply(Long stationId) throws AugeServiceException;
	
	/**
	 * 查询撤点申请单
	 * 
	 * @param applyId
	 * @return
	 * @throws AugeServiceException
	 */
	public ShutDownStationApplyDto findShutDownStationApplyById(Long applyId) throws AugeServiceException;
}
