package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.LogisticsStation;
import com.taobao.cun.auge.dal.mapper.LogisticsStationMapper;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("logisticsStationBO")
public class LogisticsStationBOImpl implements LogisticsStationBO {
	
	@Autowired
	LogisticsStationMapper logisticsStationMapper;
	
	@Override
	public void delete(Long id, String operator) throws AugeServiceException {
		ValidateUtils.notNull(id);
		ValidateUtils.notNull(operator);
		LogisticsStation rel = new LogisticsStation();
		rel.setId(id);
		DomainUtils.beforeDelete(rel, operator);
		logisticsStationMapper.updateByPrimaryKeySelective(rel);
	}

}