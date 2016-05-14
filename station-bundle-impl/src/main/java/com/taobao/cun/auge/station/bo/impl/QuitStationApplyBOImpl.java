package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.dal.mapper.QuitStationApplyMapper;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;

@Component("quitStationApplyBO")
public class QuitStationApplyBOImpl implements QuitStationApplyBO {
	
	@Autowired
	QuitStationApplyMapper quitStationApplyMapper;

	@Override
	public void saveQuitStationApply(QuitStationApply quitStationApply,String operator) {
		DomainUtils.beforeInsert(quitStationApply, operator);
		 quitStationApplyMapper.insertSelective(quitStationApply);
	}

	@Override
	public QuitStationApply findQuitStationApply(Long instanceId) {
		QuitStationApply record = new QuitStationApply();
		record.setIsDeleted("n");
		record.setPartnerInstanceId(instanceId);
		
		return quitStationApplyMapper.selectOne(record);
	}

}
