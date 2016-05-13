package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

public class StationBOImpl implements StationBO {

	private static final Logger logger = LoggerFactory.getLogger(StationBO.class);

	@Autowired
	private StationMapper stationMapper;

	@Override
	public Station getStationById(Long stationId) throws AugeServiceException {

		return stationMapper.selectByPrimaryKey(stationId);
	}

	@Override
	public Station getStationByStationNum(String stationNum) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeState(Long stationId, StationStatusEnum preStatus, StationStatusEnum postStatus, String operator)
			throws AugeServiceException {
		Station station = findStationById(stationId);

		if (!preStatus.getCode().equals(station.getStatus())) {
			logger.error("station state is not " + preStatus.getDesc());
			throw new AugeServiceException(StationExceptionEnum.STATION_STATUS_CHANGED);
		}

		Station updateStation = new Station();
		updateStation.setId(stationId);
		updateStation.setState(postStatus.getCode());

		beforeUpdate(updateStation, operator);

		stationMapper.updateByPrimaryKeySelective(updateStation);

	}

	private Station findStationById(Long stationId) throws AugeServiceException {
		Station station = stationMapper.selectByPrimaryKey(stationId);
		if (null == station) {
			logger.error("station is not exist.station id " + stationId);
			throw new AugeServiceException(StationExceptionEnum.STATION_NOT_EXIST);
		}
		return station;

	}

	private static void beforeUpdate(Station station, String operator) {
		Date now = new Date();
		station.setGmtModified(now);
		station.setModifier(operator);
	}

}
