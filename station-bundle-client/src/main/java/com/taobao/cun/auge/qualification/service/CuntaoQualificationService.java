package com.taobao.cun.auge.qualification.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;

public interface CuntaoQualificationService {

	Qualification syncCuntaoQulification(Long taobaoUserId);
	
	public Qualification syncCuntaoQulificationFromMetaq(Long taobaoUserId,Long qualiId,int eidType);
	
	Qualification queryC2BQualification(Long taobaoUserId,boolean isSyncHavana);
	
	void submitUncheckedQualification(Qualification qualification);
	
	public PageDto<Qualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition);

	public PageDto<C2BTestUser> querC2BTestUsers(CuntaoQualificationPageCondition condition);
}
