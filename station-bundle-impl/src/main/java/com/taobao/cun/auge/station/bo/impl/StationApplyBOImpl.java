package com.taobao.cun.auge.station.bo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.StationApply;
import com.taobao.cun.auge.dal.mapper.StationApplyMapper;
import com.taobao.cun.auge.station.bo.StationApplyBO;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

@Component("stationApplyBO")
public class StationApplyBOImpl implements StationApplyBO {

	private static final Logger logger = LoggerFactory.getLogger(StationApplyBO.class);

	@Autowired
	StationApplyMapper stationApplyMapper;

	@Override
	public StationApply findStationApplyById(Long stationApplyId){
		StationApply stationApply = stationApplyMapper.selectByPrimaryKey(stationApplyId);

		if (null == stationApply) {
			logger.error("station apply is not exist.instance id " + stationApplyId);
			throw new AugeBusinessException(StationExceptionEnum.STATION_APPLY_NOT_EXIST);
		}
		return stationApply;

	}
}
