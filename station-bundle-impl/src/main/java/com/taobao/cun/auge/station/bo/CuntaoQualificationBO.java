package com.taobao.cun.auge.station.bo;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.qualification.service.Qualification;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;


public interface CuntaoQualificationBO {

	public CuntaoQualification getCuntaoQualificationByTaobaoUserId(Long taobaoUserId);
	
	public void updateQualification(CuntaoQualification cuntaoQualification);
	
	public void saveQualification(CuntaoQualification cuntaoQualification);
	
	public Page<CuntaoQualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition);
	
	 Page<Long> selectC2BTestUsers(CuntaoQualificationPageCondition condition);
	 
	 public void deletedQualificationById(Long id);
	 
	 public void submitUncheckedQualification(CuntaoQualification qualification);
}
