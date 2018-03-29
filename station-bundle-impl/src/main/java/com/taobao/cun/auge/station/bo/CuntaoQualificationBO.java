package com.taobao.cun.auge.station.bo;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;


public interface CuntaoQualificationBO {

	public CuntaoQualification getCuntaoQualificationByTaobaoUserId(Long taobaoUserId);
	
	public void updateQualification(CuntaoQualification cuntaoQualification);
	
	public void saveQualification(CuntaoQualification cuntaoQualification);
	
	public Page<CuntaoQualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition);
	
	public void deletedQualificationById(Long id);
	 
	public void submitLocalQualification(CuntaoQualification qualification);
	 
	public void reSubmitLocalQualification(CuntaoQualification qualification);
	 
	public void submitHavanaQualification(Long taobaoUserId);

}
