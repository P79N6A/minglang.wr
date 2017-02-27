package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.CuntaoQualificationExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.mapper.CuntaoQualificationExtMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoQualificationMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;
@Component("cuntaoQualificationBO")
public class CuntaoQualificationBO implements com.taobao.cun.auge.station.bo.CuntaoQualificationBO {

	@Autowired
	private CuntaoQualificationMapper cuntaoQualificationMapper;
	
	@Autowired
	private CuntaoQualificationExtMapper cuntaoQualificationExtMapper;
	
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Override
	public CuntaoQualification getCuntaoQualificationByTaobaoUserId(Long taobaoUserId) {
		PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if(partnerInstance == null) return null;
		CuntaoQualificationExample example = new CuntaoQualificationExample();
		example.createCriteria().andIsDeletedEqualTo("n").andPartnerInstanceIdEqualTo(partnerInstance.getId());
		List<CuntaoQualification> qualis = cuntaoQualificationMapper.selectByExample(example);
		return ResultUtils.selectOne(qualis);
	}

	@Override
	public void updateQualification(CuntaoQualification cuntaoQualification) {
		cuntaoQualificationMapper.updateByPrimaryKeySelective(cuntaoQualification);
	}

	@Override
	public Page<CuntaoQualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition) {
		return cuntaoQualificationExtMapper.queryQualification(condition);
	}

	@Override
	public void saveQualification(CuntaoQualification cuntaoQualification) {
		cuntaoQualificationMapper.insertSelective(cuntaoQualification);
	}

}
