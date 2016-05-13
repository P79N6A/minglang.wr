package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface StationBO {
	/**
	 * 根据服务站id查询村点
	 * @param stationId
	 * @return
	 * @throws AugeServiceException
	 */
	public Station getStationById(Long stationId) throws AugeServiceException;
	
	/**
	 * 根据服务站编号查询村点
	 * @param stationNum
	 * @return
	 * @throws AugeServiceException
	 */
	public Station getStationByStationNum (String stationNum) throws AugeServiceException;

}
