package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("stationBO")
public class StationBOImpl implements StationBO {

	@Autowired
	StationMapper stationMaper;

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

	@Override
	public Long addStation(StationDto stationDto) throws AugeServiceException {

		return null;
	}

	@Override
	public Long updateStation(StationDto stationDto)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}
}
