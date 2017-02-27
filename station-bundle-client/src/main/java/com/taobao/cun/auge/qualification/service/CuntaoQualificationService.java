package com.taobao.cun.auge.qualification.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;

public interface CuntaoQualificationService {

	Qualification syncCuntaoQulification(Long taobaoUserId);
	
	Qualification queryC2BQualification(Long taobaoUserId,boolean isSyncHavana);
	
	
	public PageDto<Qualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition);

	public PageDto<C2BTestUser> querC2BTestUsers(CuntaoQualificationPageCondition condition);
}
