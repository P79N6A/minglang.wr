package com.taobao.cun.auge.station.bo.impl;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.station.enums.StationApplyStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.StationApply;
import com.taobao.cun.auge.dal.mapper.StationApplyMapper;
import com.taobao.cun.auge.station.bo.StationApplyBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

@Component("stationApplyBO")
public class StationApplyBOImpl implements StationApplyBO {

	private static final Logger logger = LoggerFactory.getLogger(StationApplyBO.class);

	@Autowired
	StationApplyMapper stationApplyMapper;

	@Override
	public StationApply findStationApplyById(Long stationApplyId) throws AugeServiceException {
		StationApply stationApply = stationApplyMapper.selectByPrimaryKey(stationApplyId);

		if (null == stationApply) {
			logger.error("station apply is not exist.instance id " + stationApplyId);
			throw new AugeServiceException(StationExceptionEnum.STATION_APPLY_NOT_EXIST);
		}
		return stationApply;

	}

	@Override
	public void changeState(Long stationApplyId, StationApplyStateEnum preState, StationApplyStateEnum postState, String operator) throws AugeServiceException {
		StationApply stationApply = stationApplyMapper.selectByPrimaryKey(stationApplyId);

		if (null == stationApply || !stationApply.getState().equals(preState.getCode())) {
			logger.error("station apply state error" + stationApplyId);
			throw new AugeServiceException("station apply state error");
		}
		StationApply updateStationApply = new StationApply();
		updateStationApply.setId(stationApplyId);
		stationApply.setState(postState.getCode());
		DomainUtils.beforeUpdate(stationApply, operator);
		stationApplyMapper.updateByPrimaryKeySelective(stationApply);
	}

}
