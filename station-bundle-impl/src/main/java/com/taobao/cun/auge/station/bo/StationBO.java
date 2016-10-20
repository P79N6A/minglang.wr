package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 *服务站表基础服务
 * @author quanzhu.wangqz
 *
 */
public interface StationBO {
	/**
	 * 根据服务站id查询村点
	 * 
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public Station getStationById(Long stationId) throws AugeServiceException;
	
	/**
	 * 根据服务站id查询村点
	 * 
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public List<Station> getStationById(List<Long> stationIds) throws AugeServiceException;

	/**
	 * 根据服务站编号查询村点
	 * 
	 * @param stationNum
	 * @return
	 * @throws AugeServiceException
	 */
	public Station getStationByStationNum(String stationNum) throws AugeServiceException;
   
	/**
     * 变更村点状态
     * @param stationId
     * @param preStatus
     * @param postStatus
     * @param operator
     * @throws AugeServiceException
     */
	public void changeState(Long stationId, StationStatusEnum preStatus, StationStatusEnum postStatus, String operator)throws AugeServiceException;

	/**
	 * 新增服务站
	 * @param stationDto
	 * @return 主键
	 * @throws AugeServiceException
	 */
	public Long addStation(StationDto stationDto) throws AugeServiceException;
	
	/**
	 * 修改服务站
	 * @param stationDto
	 * @return
	 * @throws AugeServiceException
	 */
	public void updateStation(StationDto stationDto) throws AugeServiceException;
	
	/**
	 * 检查服务站编号使用已经使用
	 * 
	 * @param stationNum
	 * @return
	 * @throws AugeServiceException
	 */
	public int getStationCountByStationNum(String stationNum) throws AugeServiceException;
	
	/**
	 * 删除服务站
	 * @param stationId
	 * @param operator
	 * @throws AugeServiceException
	 */
	public void deleteStation(Long stationId, String operator) throws AugeServiceException;

	/**
	 * 根据name、orgIdPath、stationStatusEnum查询station  为搜索tp服务站使用  只返回名字，id,村点编号
	 * @param stationCondition
	 * @return
	 * @throws AugeServiceException
	 */
	public List<Station> getStationsByName(StationCondition stationCondition) throws AugeServiceException;
	
}
