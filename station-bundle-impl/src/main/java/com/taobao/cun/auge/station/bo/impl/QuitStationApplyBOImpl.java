package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.dal.domain.QuitStationApplyExample;
import com.taobao.cun.auge.dal.domain.QuitStationApplyExample.Criteria;
import com.taobao.cun.auge.dal.mapper.QuitStationApplyMapper;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.vipserver.client.utils.CollectionUtils;

@Component("quitStationApplyBO")
public class QuitStationApplyBOImpl implements QuitStationApplyBO {

	@Autowired
	QuitStationApplyMapper quitStationApplyMapper;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void saveQuitStationApply(QuitStationApply quitStationApply, String operator) {
		DomainUtils.beforeInsert(quitStationApply, operator);
		quitStationApplyMapper.insertSelective(quitStationApply);
	}

	@Override
	public QuitStationApply findQuitStationApply(Long instanceId) {
		QuitStationApplyExample example = new QuitStationApplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerInstanceIdEqualTo(instanceId);
		List<QuitStationApply> applyes = quitStationApplyMapper.selectByExample(example);

		if (CollectionUtils.isEmpty(applyes)) {
			return null;
		}

		return applyes.get(0);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deleteQuitStationApply(Long instanceId, String operator) {
		QuitStationApplyExample example = new QuitStationApplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andPartnerInstanceIdEqualTo(instanceId);
		
		QuitStationApply record = new QuitStationApply();
		
		DomainUtils.beforeDelete(record, operator);
		quitStationApplyMapper.updateByExampleSelective(record, example);
	}
}
