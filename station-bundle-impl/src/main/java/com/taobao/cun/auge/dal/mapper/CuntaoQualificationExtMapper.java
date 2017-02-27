package com.taobao.cun.auge.dal.mapper;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;

public interface CuntaoQualificationExtMapper {

	 int countQualification(CuntaoQualificationPageCondition condition);
	
	 Page<CuntaoQualification> queryQualification(CuntaoQualificationPageCondition condition);
}
