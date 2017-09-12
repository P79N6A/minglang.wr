package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.logistics.dto.LogisticsStationDto;
import com.taobao.cun.auge.logistics.dto.LogisticsStationQueryDto;

/**
 * 物流基础服务
 * @author quanzhu.wangqz
 *
 */
public interface LogisticsStationBO {

	/**
	 * 根据主键删除
	 * @param id
	 * @param operator
	 * @
	 */
	public void delete(Long id,String operator) ;
	
	
	public void changeState(Long id,String operator,String targetState) ;
	
	/**
	 * 添加菜鸟物流站点
	 * 
	 * @param stationDto
	 */
	public Long addLogisticsStation(LogisticsStationDto stationDto);

	/**
	 * 删除菜鸟物流站点
	 * 
	 * @param cainiaoStationId
	 *            菜鸟站点id
	 * @param modifier
	 * @return
	 */
	public Boolean deleteLogisticsStation(Long cainiaoStationId, String modifier);

	/**
	 * 修改菜鸟物流站点
	 * 
	 * @param stationDto
	 * @return
	 */
	public Boolean updateLogisticsStation(LogisticsStationDto stationDto);

	/**
	 * 查询单个菜鸟物流站点
	 * 
	 * @param queryDto
	 * @return
	 */
	public LogisticsStationDto findLogisticsStation(LogisticsStationQueryDto queryDto);

	/**
	 * 分页查询菜鸟物流站点
	 * 
	 * @param queryDto
	 * @return
	 */
	public PageDto<LogisticsStationDto> findLogisticsStationByPage(LogisticsStationQueryDto queryDto);
	
	/**
	 * 查询带审核的LogisticStation
	 * @param taobaoUserId
	 * @return
	 */
	public LogisticsStationDto findToAuditLogisticsStationByTaobaoUserId(Long taobaoUserId);
	
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public LogisticsStationDto findLogisticStation(Long id);
	
}
