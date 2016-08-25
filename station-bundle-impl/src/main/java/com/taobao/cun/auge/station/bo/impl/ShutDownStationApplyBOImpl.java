package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.ShutDownStationApply;
import com.taobao.cun.auge.dal.domain.ShutDownStationApplyExample;
import com.taobao.cun.auge.dal.domain.ShutDownStationApplyExample.Criteria;
import com.taobao.cun.auge.dal.mapper.ShutDownStationApplyMapper;
import com.taobao.cun.auge.station.bo.ShutDownStationApplyBO;
import com.taobao.vipserver.client.utils.CollectionUtils;

@Component("shutDownStationApplyBO")
public class ShutDownStationApplyBOImpl implements ShutDownStationApplyBO{

	@Autowired
	ShutDownStationApplyMapper  shutDownStationApplyMapper;

	@Override
	public void saveShutDownStationApply(ShutDownStationApply shutDownStationApply, String operator) {
		DomainUtils.beforeInsert(shutDownStationApply, operator);
		shutDownStationApplyMapper.insertSelective(shutDownStationApply);
	}

	@Override
	public ShutDownStationApply findShutDownStationApply(Long stationId) {
		ShutDownStationApplyExample example = new ShutDownStationApplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStationIdEqualTo(stationId);
		List<ShutDownStationApply> applyes = shutDownStationApplyMapper.selectByExample(example);

		if (CollectionUtils.isEmpty(applyes)) {
			return null;
		}

		return applyes.get(0);
	}

	@Override
	public void deleteShutDownStationApply(Long stationId, String operator) {
		ShutDownStationApplyExample example = new ShutDownStationApplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andStationIdEqualTo(stationId);
		criteria.andIsDeletedEqualTo("n");
		
		ShutDownStationApply record = new ShutDownStationApply();
		
		DomainUtils.beforeDelete(record, operator);
		shutDownStationApplyMapper.updateByExampleSelective(record, example);
	}
}
