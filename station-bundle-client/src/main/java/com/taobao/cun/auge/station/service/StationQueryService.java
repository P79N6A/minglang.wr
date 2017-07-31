package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;

/**
 * 服务站查询接口
 */
public interface StationQueryService {
	
	/**
	 * 根据stationId,查询村点信息
	 * 
	 * @param stationId
	 * @return
	 */
	public StationDto getStation(Long stationId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param stationCondition
	 * @return
	 */
	public StationDto queryStationInfo(StationCondition stationCondition);

	/**
	 * 根据station id查询station
	 * 
	 * @param stationIds
	 * @return
	 */
	public List<StationDto> queryStations(List<Long> stationIds);
	
	/**
	 * 根据name、orgId、stationStatusEnum查询station
	 * 
	 * @param stationCondition
	 * @return
	 */
	public PageDto<StationDto> queryStations(StationCondition stationCondition);

	/**
	 * 根据name、orgIdPath、stationStatusEnum查询TPstation
	 * 
	 * @param stationCondition
	 * @return
	 */
	public List<StationDto> getTpStationsByName(StationCondition stationCondition);

	/**
	 * 查询撤点申请单
	 * 
	 * @param stationId
	 * @return
	 */
	public ShutDownStationApplyDto findShutDownStationApply(Long stationId);
	
	/**
	 * 查询撤点申请单
	 * 
	 * @param applyId
	 * @return
	 */
	public ShutDownStationApplyDto findShutDownStationApplyById(Long applyId);
}
