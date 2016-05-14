package com.taobao.cun.auge.station.bo.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.conversion.PartnerLifecycleConverter;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.mapper.PartnerLifecycleItemsMapper;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.condition.PartnerLifecycleCondition;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

public class PartnerLifecycleBOImpl implements PartnerLifecycleBO {
	
	@Autowired
	PartnerLifecycleItemsMapper partnerLifecycleItemsMapper;
	
	@Override
	public void addLifecycle(PartnerLifecycleCondition lifecycle,String operator) throws AugeServiceException{
		if (lifecycle ==null || StringUtils.isEmpty(operator)){
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerLifecycleItems items = PartnerLifecycleConverter.ConvertDomain(lifecycle);
		DomainUtils.beforeInsert(items, operator, PartnerLifecycleItems.class);
		partnerLifecycleItemsMapper.insert(items);
	}
}
