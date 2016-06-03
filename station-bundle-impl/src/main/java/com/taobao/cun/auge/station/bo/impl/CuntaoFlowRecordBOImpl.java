package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.dal.mapper.CuntaoFlowRecordMapper;
import com.taobao.cun.auge.station.bo.CuntaoFlowRecordBO;

@Component("cuntaoFlowRecordBO")
public class CuntaoFlowRecordBOImpl implements CuntaoFlowRecordBO{
	
	@Autowired
	CuntaoFlowRecordMapper cuntaoFlowRecordMapper;
	
	@Override
	public void addRecord(CuntaoFlowRecord record) {
		DomainUtils.beforeInsert(record, record.getOperatorWorkid());
		cuntaoFlowRecordMapper.insertSelective(record);
		
	}

}