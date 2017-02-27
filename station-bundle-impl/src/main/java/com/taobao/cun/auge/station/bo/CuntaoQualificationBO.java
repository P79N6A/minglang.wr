package com.taobao.cun.auge.station.bo;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;


public interface CuntaoQualificationBO {

	public CuntaoQualification getCuntaoQualificationByTaobaoUserId(Long taobaoUserId);
	
	public void updateQualification(CuntaoQualification cuntaoQualification);
	
	public void saveQualification(CuntaoQualification cuntaoQualification);
	
	public Page<CuntaoQualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition);
}
