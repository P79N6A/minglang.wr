package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 服务站查询接口
 * @author quanzhu.wangqz
 *
 */
public interface StationQueryService {
	/**
	 * 根据主键查询
	 * @param stationCondition
	 * @return
	 * @throws AugeServiceException
	 */
	public StationDto queryStationInfo(StationCondition stationCondition) throws AugeServiceException;
}
