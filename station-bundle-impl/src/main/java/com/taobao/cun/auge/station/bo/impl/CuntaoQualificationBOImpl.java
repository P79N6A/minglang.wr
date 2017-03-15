package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.CuntaoQualificationExample;
import com.taobao.cun.auge.dal.mapper.CuntaoQualificationMapper;
import com.taobao.cun.auge.qualification.service.QualificationStatus;
import com.taobao.cun.auge.station.adapter.SellerQualiServiceAdapter;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;
@Component("cuntaoQualificationBO")
public class CuntaoQualificationBOImpl implements CuntaoQualificationBO {

	@Autowired
	private CuntaoQualificationMapper cuntaoQualificationMapper;
	
	@Autowired
	private SellerQualiServiceAdapter sellerQualiServiceAdapter;
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
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateQualification(CuntaoQualification cuntaoQualification) {
		if(cuntaoQualification.getStatus() == QualificationStatus.VALID){
			CuntaoQualification record = new CuntaoQualification();
			record.setId(cuntaoQualification.getId());
			record.setErrorCode(null);
			record.setErrorMessage(null);
			cuntaoQualificationMapper.updateByPrimaryKey(record);
		}
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
		if(qualification.getId()==null){
			qualification.setStatus(QualificationStatus.UN_SUBMIT);
			DomainUtils.beforeInsert(qualification, "system");
			cuntaoQualificationMapper.insertSelective(qualification);
		}else{
			DomainUtils.beforeUpdate(qualification, "system");
			cuntaoQualificationMapper.updateByPrimaryKeySelective(qualification);
		}
	}


	@Override
	public void submitHavanaQualification(Long taobaoUserId) {
		CuntaoQualification cuntaoQualification = this.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
		if(cuntaoQualification.getStatus() == QualificationStatus.SUBMIT_FAIL||cuntaoQualification.getStatus() == QualificationStatus.UN_SUBMIT){
			sellerQualiServiceAdapter.insertQualiRecord(cuntaoQualification);
			cuntaoQualificationMapper.updateByPrimaryKeySelective(cuntaoQualification);
		}
	}

}
