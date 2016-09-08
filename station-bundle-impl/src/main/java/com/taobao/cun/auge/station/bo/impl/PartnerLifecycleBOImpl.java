package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.StringUtil;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItemsExample;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItemsExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerLifecycleItemsMapper;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.convert.PartnerLifecycleConverter;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCourseStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

@Component("partnerLifecycleBO")
public class PartnerLifecycleBOImpl implements PartnerLifecycleBO {

	@Autowired
	PartnerLifecycleItemsMapper partnerLifecycleItemsMapper;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void addLifecycle(PartnerLifecycleDto partnerLifecycleDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerLifecycleDto);
		
		PartnerLifecycleItems items = PartnerLifecycleConverter.toPartnerLifecycleItems(partnerLifecycleDto);
		if(StringUtil.isEmpty(partnerLifecycleDto.getOperator())){
			DomainUtils.beforeInsert(items, DomainUtils.DEFAULT_OPERATOR);
		}else{
			DomainUtils.beforeInsert(items, partnerLifecycleDto.getOperator());
		}
		partnerLifecycleItemsMapper.insert(items);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateLifecycle(PartnerLifecycleDto partnerLifecycleDto) throws AugeServiceException {
		ValidateUtils.notNull(partnerLifecycleDto);
		ValidateUtils.notNull(partnerLifecycleDto.getLifecycleId() );
	
		PartnerLifecycleItems items = PartnerLifecycleConverter.toPartnerLifecycleItems(partnerLifecycleDto);
		if(StringUtil.isEmpty(partnerLifecycleDto.getOperator())){
			DomainUtils.beforeUpdate(items, DomainUtils.DEFAULT_OPERATOR);
		}else{
			DomainUtils.beforeUpdate(items, partnerLifecycleDto.getOperator());
		}
		partnerLifecycleItemsMapper.updateByPrimaryKeySelective(items);
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
		ValidateUtils.notNull(businessTypeEnum);
		ValidateUtils.notNull(instanceId);
		ValidateUtils.notNull(stepEnum);
		
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
		ValidateUtils.notNull(businessTypeEnum);
		ValidateUtils.notNull(instanceId);

		PartnerLifecycleItemsExample example = new PartnerLifecycleItemsExample();
		Criteria criteria = example.createCriteria();
		criteria.andPartnerInstanceIdEqualTo(instanceId);
		criteria.andIsDeletedEqualTo("n");
		criteria.andBusinessTypeEqualTo(businessTypeEnum.getCode());
		criteria.andCurrentStepEqualTo(PartnerLifecycleCurrentStepEnum.PROCESSING.getCode());
		return ResultUtils.selectOne(partnerLifecycleItemsMapper.selectByExample(example));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
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

	@Override
	public void updateDecorateState(Long instanceId,
			PartnerLifecycleDecorateStatusEnum decorateStateEnum,
			OperatorDto operatorDto) throws AugeServiceException {
		ValidateUtils.notNull(instanceId);
		ValidateUtils.notNull(operatorDto);
		ValidateUtils.notNull(decorateStateEnum);
		
		PartnerLifecycleItems items = this.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.DECORATING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);
		if (items == null) {
			throw new AugeServiceException(CommonExceptionEnum.DATA_UNNORMAL);
		}
		
		PartnerLifecycleDto param = new PartnerLifecycleDto();
		param.setDecorateStatus(decorateStateEnum);
		param.setLifecycleId(items.getId());
		param.copyOperatorDto(operatorDto);
	/*	if (PartnerLifecycleCourseStatusEnum.DONE.getCode().equals(items.getCourseStatus())
				&& PartnerLifecycleDecorateStatusEnum.DONE.equals(decorateStateEnum)) {
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
		}*/
		
		this.updateLifecycle(param);
	}

	@Override
	public void updateCourseState(Long instanceId,
			PartnerLifecycleCourseStatusEnum courseStatusEnum, OperatorDto operatorDto)
			throws AugeServiceException {
		ValidateUtils.notNull(instanceId);
		ValidateUtils.notNull(operatorDto);
		ValidateUtils.notNull(courseStatusEnum);
		
		PartnerLifecycleItems items = this.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.DECORATING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);
		if (items == null) {
			throw new AugeServiceException(CommonExceptionEnum.DATA_UNNORMAL);
		}
		
		PartnerLifecycleDto param = new PartnerLifecycleDto();
		param.setCourseStatus(courseStatusEnum);
		param.setLifecycleId(items.getId());
		param.copyOperatorDto(operatorDto);
/*		if (PartnerLifecycleCourseStatusEnum.DONE.equals(courseStatusEnum)
				&& PartnerLifecycleDecorateStatusEnum.DONE.getCode().equals(items.getDecorateStatus())) {
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
		}*/
		
		this.updateLifecycle(param);
	}
}
