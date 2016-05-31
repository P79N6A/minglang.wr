package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItemsExample;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItemsExample.Criteria;
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
	public void addLifecycle(PartnerLifecycleDto partnerLifecycleDto) throws AugeServiceException {
		if (partnerLifecycleDto == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerLifecycleItems items = PartnerLifecycleConverter.toPartnerLifecycleItems(partnerLifecycleDto);
		DomainUtils.beforeInsert(items, DomainUtils.DEFAULT_OPERATOR);
		partnerLifecycleItemsMapper.insert(items);
	}

	public void updateLifecycle(PartnerLifecycleDto partnerLifecycleDto) throws AugeServiceException {
		if (partnerLifecycleDto == null || partnerLifecycleDto.getLifecycleId() == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerLifecycleItems items = PartnerLifecycleConverter.toPartnerLifecycleItems(partnerLifecycleDto);
		DomainUtils.beforeUpdate(items, DomainUtils.DEFAULT_OPERATOR);
		partnerLifecycleItemsMapper.updateByPrimaryKey(items);
	}

	public Long getLifecycleItemsId(Long instanceId, PartnerLifecycleBusinessTypeEnum businessTypeEnum,
			PartnerLifecycleCurrentStepEnum stepEnum) throws AugeServiceException {
		PartnerLifecycleItems items = getLifecycleItems(instanceId, businessTypeEnum, stepEnum);
		if (items != null) {
			return items.getId();
		}
		return null;
	}

	public PartnerLifecycleItems getLifecycleItems(Long instanceId, PartnerLifecycleBusinessTypeEnum businessTypeEnum,
			PartnerLifecycleCurrentStepEnum stepEnum) throws AugeServiceException {
		if (instanceId == null || businessTypeEnum == null || stepEnum == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerLifecycleItemsExample example = new PartnerLifecycleItemsExample();

		Criteria criteria = example.createCriteria();
		criteria.andPartnerInstanceIdEqualTo(instanceId);
		criteria.andIsDeletedEqualTo("n");
		criteria.andBusinessTypeEqualTo(businessTypeEnum.getCode());
		criteria.andCurrentStepEqualTo(stepEnum.getCode());

		return ResultUtils.selectOne(partnerLifecycleItemsMapper.selectByExample(example));
	}

	@Override
	public PartnerLifecycleItems getLifecycleItems(Long instanceId, PartnerLifecycleBusinessTypeEnum businessTypeEnum)
			throws AugeServiceException {
		if (instanceId == null || businessTypeEnum == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}

		PartnerLifecycleItemsExample example = new PartnerLifecycleItemsExample();
		Criteria criteria = example.createCriteria();
		criteria.andPartnerInstanceIdEqualTo(instanceId);
		criteria.andIsDeletedEqualTo("n");
		criteria.andBusinessTypeEqualTo(businessTypeEnum.getCode());
		criteria.andCurrentStepEqualTo(PartnerLifecycleCurrentStepEnum.END.getCode());
		return ResultUtils.selectOne(partnerLifecycleItemsMapper.selectByExample(example));
	}

	@Override
	public void deleteLifecycleItems(Long instanceId, String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(instanceId);
		ValidateUtils.notNull(operator);
		PartnerLifecycleItems record = new PartnerLifecycleItems();
		DomainUtils.beforeDelete(record, operator);
		
		PartnerLifecycleItemsExample example = new PartnerLifecycleItemsExample();
		Criteria criteria = example.createCriteria();
		criteria.andPartnerInstanceIdEqualTo(instanceId);
		criteria.andIsDeletedEqualTo("n");
		
		partnerLifecycleItemsMapper.updateByExampleSelective(record, example);
	}
}
