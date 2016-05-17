package com.taobao.cun.auge.station.bo.impl;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("stationBO")
public class StationBOImpl implements StationBO {

	@Override
	public Station getStationById(Long stationId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getParentOrgId(Long stationId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Station getStationByStationNum(String stationNum)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeState(Long stationId, StationStatusEnum preStatus,
			StationStatusEnum postStatus, String operator)
			throws AugeServiceException {
		// TODO Auto-generated method stub

	}

}
