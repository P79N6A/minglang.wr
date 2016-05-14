package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.conversion.PartnerLifecycleConverter;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.mapper.PartnerLifecycleItemsMapper;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.condition.PartnerLifecycleCondition;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

public class PartnerLifecycleBOImpl implements PartnerLifecycleBO {
	
	@Autowired
	PartnerLifecycleItemsMapper partnerLifecycleItemsMapper;
	
	@Override
	public void addLifecycle(PartnerLifecycleCondition lifecycle) throws AugeServiceException{
		if (lifecycle ==null){
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerLifecycleItems items = PartnerLifecycleConverter.convertToDomain(lifecycle);
		DomainUtils.beforeInsert(items, DomainUtils.DEFAULT_OPERATOR);
		partnerLifecycleItemsMapper.insert(items);
	}
	
	public void updateLifecycle(PartnerLifecycleCondition lifecycle)throws AugeServiceException{
		if (lifecycle ==null  || lifecycle.getLifecycleId() ==null){
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerLifecycleItems items = PartnerLifecycleConverter.convertToDomain(lifecycle);
		DomainUtils.beforeUpdate(items, DomainUtils.DEFAULT_OPERATOR);
		partnerLifecycleItemsMapper.updateByPrimaryKey(items);
	}
	
	public Long getLifecycleItemsId(Long instanceId,PartnerLifecycleBusinessTypeEnum businessTypeEnum, PartnerLifecycleCurrentStepEnum stepEnum) throws AugeServiceException {
		PartnerLifecycleItems items = getLifecycleItems(instanceId,businessTypeEnum,stepEnum);
		if (items != null) {
			return items.getId();
		}
		return null;
	}
	
	public PartnerLifecycleItems getLifecycleItems(Long instanceId,PartnerLifecycleBusinessTypeEnum businessTypeEnum, PartnerLifecycleCurrentStepEnum stepEnum) throws AugeServiceException {
		if (instanceId ==null  || businessTypeEnum == null || stepEnum ==null){
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerLifecycleItems items = new PartnerLifecycleItems();
		items.setPartnerInstanceId(instanceId);
		items.setBusinessType(businessTypeEnum.getCode());
		items.setCurrentStep(stepEnum.getCode());
		return partnerLifecycleItemsMapper.selectOne(items);
	}
}
