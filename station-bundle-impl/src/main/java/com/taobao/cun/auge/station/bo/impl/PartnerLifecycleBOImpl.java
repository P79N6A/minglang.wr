package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.mapper.PartnerLifecycleItemsMapper;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.convert.PartnerLifecycleConverter;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

@Component("partnerLifecycleBO")
public class PartnerLifecycleBOImpl implements PartnerLifecycleBO {
	
	@Autowired
	PartnerLifecycleItemsMapper partnerLifecycleItemsMapper;
	
	@Override
	public void addLifecycle(PartnerLifecycleDto partnerLifecycleDto) throws AugeServiceException{
		if (partnerLifecycleDto ==null){
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerLifecycleItems items = PartnerLifecycleConverter.toPartnerLifecycleItems(partnerLifecycleDto);
		DomainUtils.beforeInsert(items, DomainUtils.DEFAULT_OPERATOR);
		partnerLifecycleItemsMapper.insert(items);
	}
	
	public void updateLifecycle(PartnerLifecycleDto partnerLifecycleDto)throws AugeServiceException{
		if (partnerLifecycleDto ==null  || partnerLifecycleDto.getLifecycleId() ==null){
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerLifecycleItems items = PartnerLifecycleConverter.toPartnerLifecycleItems(partnerLifecycleDto);
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

	@Override
	public PartnerLifecycleItems getLifecycleItems(Long instanceId,
			PartnerLifecycleBusinessTypeEnum businessTypeEnum)
			throws AugeServiceException {
		if (instanceId ==null  || businessTypeEnum == null){
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		
		Example example = new Example(PartnerLifecycleItems.class);
		Criteria criteria = example.createCriteria();
		criteria.andCondition("isDeleted","n");
		criteria.andCondition("instanceId",instanceId);
		criteria.andCondition("businessType",businessTypeEnum.getCode());
		criteria.andNotEqualTo("currentStep", PartnerLifecycleCurrentStepEnum.END.getCode());
		List<PartnerLifecycleItems> resList = partnerLifecycleItemsMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(resList)) {
			return resList.get(0);
		}
		return null;
	}
}
