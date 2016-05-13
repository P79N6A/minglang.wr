package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public class StationBOImpl implements StationBO {
	
	@Autowired
	private StationMapper stationMapper;
	
	@Override
	public Station getStationById(Long stationId) throws AugeServiceException {
		
		return stationMapper.selectByPrimaryKey(stationId);
	}

	@Override
	public Station getStationByStationNum(String stationNum)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
