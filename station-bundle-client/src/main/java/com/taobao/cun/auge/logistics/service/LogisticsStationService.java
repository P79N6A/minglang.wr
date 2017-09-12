package com.taobao.cun.auge.logistics.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.logistics.dto.LogisticsStationDto;
import com.taobao.cun.auge.logistics.dto.LogisticsStationQueryDto;
import com.taobao.cun.crius.common.resultmodel.ResultModel;

public interface LogisticsStationService {

	/**
	 * 添加菜鸟物流站点
	 * 
	 * @param stationDto
	 */
	public ResultModel<Long> addLogisticsStation(LogisticsStationDto stationDto);

	/**
	 * 删除菜鸟物流站点
	 * 
	 * @param cainiaoStationId 菜鸟站点id
	 * @param modifier
	 * @return
	 */
	public ResultModel<Boolean> deleteLogisticsStation(Long cainiaoStationId, String modifier);
	
	/**
	 * 修改菜鸟物流站点
	 * 
	 * @param stationDto
	 * @return
	 */
	public ResultModel<Boolean> updateLogisticsStation(LogisticsStationDto stationDto);
	
	

	/**
	 * 查询单个菜鸟物流站点
	 * 
	 * @param queryDto
	 * @return
	 */
	public ResultModel<LogisticsStationDto> findLogisticsStation(LogisticsStationQueryDto queryDto);

	/**
	 * 分页查询菜鸟物流站点
	 * 
	 * @param queryDto
	 * @return
	 */
	public PageDto<LogisticsStationDto> findLogisticsStationByPage(LogisticsStationQueryDto queryDto);
	
	/**
	 * 查询单个菜鸟物流站点
	 * 
	 * @param queryDto
	 * @return
	 */
	public ResultModel<LogisticsStationDto> findLogisticsStation(Long cainiaoStationId);
	
	/**
	 * 查询单个菜鸟物流站点
	 * 
	 * @param queryDto
	 * @return
	 */
	public  ResultModel<LogisticsStationDto> findLogisticsStationByStationId(Long stationId);
	
	/**
	 * 
	 * 
	 * @param queryDto
	 * @return
	 */
	public ResultModel<Long> findStationIdByCainiaoStationId(Long cainiaoStationId);
}
