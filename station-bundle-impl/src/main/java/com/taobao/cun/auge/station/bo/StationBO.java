package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

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
	 * 根据stationId,查询村点所属组织id
	 * 
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public Long getParentOrgId(Long stationId) throws AugeServiceException;

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
	 * @return
	 * @throws AugeServiceException
	 */
	public Long addStation(StationDto stationDto) throws AugeServiceException;
	
	/**
	 * 修改服务站
	 * @param stationDto
	 * @return
	 * @throws AugeServiceException
	 */
	public Long updateStation(StationDto stationDto) throws AugeServiceException;

}
