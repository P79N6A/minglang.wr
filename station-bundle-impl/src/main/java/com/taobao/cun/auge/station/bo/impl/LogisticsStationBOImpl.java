package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.LogisticsStation;
import com.taobao.cun.auge.dal.mapper.LogisticsStationMapper;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;

@Component("logisticsStationBO")
public class LogisticsStationBOImpl implements LogisticsStationBO {
	
	@Autowired
	LogisticsStationMapper logisticsStationMapper;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void delete(Long id, String operator)  {
		ValidateUtils.notNull(id);
		ValidateUtils.notNull(operator);
		LogisticsStation rel = new LogisticsStation();
		rel.setId(id);
		DomainUtils.beforeDelete(rel, operator);
		logisticsStationMapper.updateByPrimaryKeySelective(rel);
	}

	@Override
	public void changeState(Long id, String operator,String targetState)  {
		ValidateUtils.notNull(id);
		ValidateUtils.notNull(operator);
		ValidateUtils.notNull(targetState);
		LogisticsStation rel = new LogisticsStation();
		rel.setId(id);
		rel.setState(targetState);
		DomainUtils.beforeUpdate(rel, operator);
		logisticsStationMapper.updateByPrimaryKeySelective(rel);
	}

}
