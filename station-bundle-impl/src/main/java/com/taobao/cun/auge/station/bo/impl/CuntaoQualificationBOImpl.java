package com.taobao.cun.auge.station.bo.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.CuntaoQualificationExample;
import com.taobao.cun.auge.dal.domain.CuntaoQualificationHistory;
import com.taobao.cun.auge.dal.mapper.CuntaoQualificationHistoryMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoQualificationMapper;
import com.taobao.cun.auge.qualification.service.QualificationBuilder;
import com.taobao.cun.auge.qualification.service.QualificationStatus;
import com.taobao.cun.auge.station.adapter.SellerQualiServiceAdapter;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
@Component("cuntaoQualificationBO")
public class CuntaoQualificationBOImpl implements CuntaoQualificationBO {

	@Autowired
	private CuntaoQualificationMapper cuntaoQualificationMapper;
	
	@Autowired
	private CuntaoQualificationHistoryMapper cuntaoQualificationHistoryMapper;
	
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
		cuntaoQualificationMapper.updateByPrimaryKeySelective(cuntaoQualification);
		if(Objects.equals(cuntaoQualification.getStatus(), QualificationStatus.VALID)){
			CuntaoQualification record = new CuntaoQualification();
			record.setId(cuntaoQualification.getId());
			record.setErrorCode("");
			record.setErrorMessage("");
			cuntaoQualificationMapper.updateByPrimaryKeySelective(record);
		}
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
			qualification.setStatus(QualificationStatus.UN_SUBMIT);
			qualification.setErrorCode("");
			qualification.setErrorMessage("");
			cuntaoQualificationMapper.updateByPrimaryKeySelective(qualification);
		}
	}


	@Override
	public void submitHavanaQualification(Long taobaoUserId) {
		CuntaoQualification cuntaoQualification = this.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
		if(Objects.equals(cuntaoQualification.getStatus(), QualificationStatus.SUBMIT_FAIL) || Objects.equals(
            cuntaoQualification.getStatus(), QualificationStatus.UN_SUBMIT)){
			DomainUtils.beforeUpdate(cuntaoQualification, "system");
			sellerQualiServiceAdapter.insertQualiRecord(cuntaoQualification);
			cuntaoQualificationMapper.updateByPrimaryKeySelective(cuntaoQualification);
		}
	}

	@Override
	public void reSubmitLocalQualification(CuntaoQualification qualification) {
		CuntaoQualification  invalidQuali = this.getCuntaoQualificationByTaobaoUserId(qualification.getTaobaoUserId());
		if(invalidQuali != null && "1".equals(invalidQuali.getUpdateFlag())){
			CuntaoQualificationHistory record = new CuntaoQualificationHistory();
			record.setTaobaoUserId(invalidQuali.getTaobaoUserId());
			record.setCreator("system");
			record.setGmtCreate(new Date());
			record.setGmtModified(new Date());
			record.setModifier("system");
			record.setIsDeleted("n");
			record.setCompanyName(invalidQuali.getCompanyName());
			record.setBizScope(invalidQuali.getBizScope());
			record.setLegalPerson(invalidQuali.getLegalPerson());
			record.setQualiNo(invalidQuali.getQualiNo());
			record.setQualiPic(invalidQuali.getQualiPic());
			record.setRegsiterAddress(invalidQuali.getRegsiterAddress());
			cuntaoQualificationHistoryMapper.insertSelective(record);
			
			CuntaoQualification deletedQuali = new CuntaoQualification();
			deletedQuali.setIsDeleted("y");
			deletedQuali.setId(invalidQuali.getId());
			record.setGmtModified(new Date());
			record.setModifier("system");
			cuntaoQualificationMapper.updateByPrimaryKeySelective(deletedQuali);
			qualification.setId(null);
		}
		this.submitLocalQualification(qualification);
	}

	@Override
	public List<CuntaoQualification> queryHistoriesByTaobaoUserId(Long taobaoUserId) {
		CuntaoQualificationExample example = new CuntaoQualificationExample();
		example.setOrderByClause("id desc");
		example.createCriteria().andIsDeletedEqualTo("y")
		.andEnterpriceTypeIn(Lists.newArrayList(QualificationBuilder.BIG_BUSINESS,QualificationBuilder.SMALL_BUSINESS))
		.andTaobaoUserIdEqualTo(taobaoUserId);
		List<CuntaoQualification> qualifications = cuntaoQualificationMapper.selectByExample(example);
		return qualifications;
	}

	@Override
	public Boolean checkValidQualification(Long taobaoUserId) {
		CuntaoQualification q = this.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
		if (q== null) {
			return Boolean.FALSE;
		}
		if ( q.getStatus() != null && new Integer(1).equals(q.getStatus())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
