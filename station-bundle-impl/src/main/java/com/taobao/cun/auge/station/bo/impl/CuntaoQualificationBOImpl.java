package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.CuntaoQualificationExample;
import com.taobao.cun.auge.dal.mapper.CuntaoQualificationExtMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoQualificationMapper;
import com.taobao.cun.auge.qualification.service.QualificationStatus;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;
@Component("cuntaoQualificationBO")
public class CuntaoQualificationBOImpl implements CuntaoQualificationBO {

	@Autowired
	private CuntaoQualificationMapper cuntaoQualificationMapper;
	
	@Override
	public CuntaoQualification getCuntaoQualificationByTaobaoUserId(Long taobaoUserId) {
		CuntaoQualificationExample example = new CuntaoQualificationExample();
		example.createCriteria().andTaobaoUserIdEqualTo(taobaoUserId).andIsDeletedEqualTo("n");
		List<CuntaoQualification> qualis = cuntaoQualificationMapper.selectByExample(example);
		return ResultUtils.selectOne(qualis);
	}
	
	@Override
	public void deletedQualificationById(Long id){
		CuntaoQualification record = new CuntaoQualification();
		record.setIsDeleted("y");
		record.setId(id);
		cuntaoQualificationMapper.updateByPrimaryKeySelective(record);
	}
	
	
	@Override
	public void updateQualification(CuntaoQualification cuntaoQualification) {
		cuntaoQualificationMapper.updateByPrimaryKeySelective(cuntaoQualification);
	}

	@Override
	public Page<CuntaoQualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition) {
		PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
		CuntaoQualificationExample example = new CuntaoQualificationExample();
		example.createCriteria().andIsDeletedEqualTo("n").andStatusIn(condition.getStatusList());
		return (Page<CuntaoQualification>) cuntaoQualificationMapper.selectByExample(example);
	}

	@Override
	public void saveQualification(CuntaoQualification cuntaoQualification) {
		cuntaoQualificationMapper.insertSelective(cuntaoQualification);
	}


	@Override
	public void submitLocalQualification(CuntaoQualification qualification) {
		DomainUtils.beforeInsert(qualification, "system");
		qualification.setStatus(-1);
		cuntaoQualificationMapper.insert(qualification);
		
	}

	@Override
	public CuntaoQualification getCuntaoQualificationById(Long id) {
		return cuntaoQualificationMapper.selectByPrimaryKey(id);
	}

	@Override
	public void submitHavanaQualification(Long cuntaoQualificationId) {
		CuntaoQualification cuntaoQualification = this.getCuntaoQualificationById(cuntaoQualificationId);
		if(cuntaoQualification.getStatus() == QualificationStatus.SUBMIT_FAIL||cuntaoQualification.getStatus() == QualificationStatus.UN_SUBMIT){
			//checkQualification
			//submitQualificationToHavana
		}
	}

}
