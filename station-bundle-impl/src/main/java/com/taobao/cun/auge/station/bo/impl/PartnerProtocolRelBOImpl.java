package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRel;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExample;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerProtocolRelMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.convert.PartnerProtocolRelConverter;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

@Component("partnerProtocolRelBO")
public class PartnerProtocolRelBOImpl implements PartnerProtocolRelBO {

	@Autowired
	PartnerProtocolRelMapper partnerProtocolRelMapper;

	@Autowired
	ProtocolBO protocolBO;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void signProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId, PartnerProtocolRelTargetTypeEnum targetType) {
		Date now = new Date();
		signProtocol(businessId, taobaoUserId, type, now, now, null, String.valueOf(taobaoUserId),
				PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void signProtocol(Long objectId, Long taobaoUserId, ProtocolTypeEnum type, Date confirmTime, Date startTime, Date endTime,
			String operator, PartnerProtocolRelTargetTypeEnum targetType) {
		Long protocolId = protocolBO.getValidProtocol(type).getId();
		if (null == protocolId) {
			throw new RuntimeException("protocol not exists: " + type);
		}

		PartnerProtocolRel partnerProtocolRelDO = new PartnerProtocolRel();

		partnerProtocolRelDO.setTaobaoUserId(taobaoUserId);
		partnerProtocolRelDO.setProtocolId(protocolId);
		partnerProtocolRelDO.setObjectId(objectId);
		partnerProtocolRelDO.setTargetType(targetType.getCode());
		partnerProtocolRelDO.setConfirmTime(confirmTime);
		partnerProtocolRelDO.setStartTime(startTime);
		partnerProtocolRelDO.setEndTime(endTime);

		DomainUtils.beforeInsert(partnerProtocolRelDO, operator);

		partnerProtocolRelMapper.insertSelective(partnerProtocolRelDO);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void cancelProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId, PartnerProtocolRelTargetTypeEnum targetType,
			String operator) {
		PartnerProtocolRelExample example = new PartnerProtocolRelExample();

		Criteria criteria = example.createCriteria();

		criteria.andTaobaoUserIdEqualTo(taobaoUserId);
		criteria.andTargetTypeEqualTo(targetType.getCode());
		criteria.andObjectIdEqualTo(businessId);
		criteria.andIsDeletedEqualTo("n");

		Long protocolId = protocolBO.getValidProtocol(type).getId();
		criteria.andProtocolIdEqualTo(protocolId);

		PartnerProtocolRel  record = new PartnerProtocolRel();
		DomainUtils.beforeDelete(record, operator);

		partnerProtocolRelMapper.updateByExampleSelective(record, example);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long addPartnerProtocolRel(
			PartnerProtocolRelDto partnerProtocolRelDto)
			 {
		ValidateUtils.notNull(partnerProtocolRelDto);
		PartnerProtocolRel record = PartnerProtocolRelConverter.toPartnerProtocolRel(partnerProtocolRelDto);
		if (partnerProtocolRelDto.getProtocolId() == null && partnerProtocolRelDto.getProtocolTypeEnum() != null) {
			Long protocolId = protocolBO.getValidProtocol(partnerProtocolRelDto.getProtocolTypeEnum()).getId();
			if (protocolId == null) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"protocolBO.getValidProtocol is null");
			}
			record.setProtocolId(protocolId);
		}
		
		DomainUtils.beforeInsert(record, partnerProtocolRelDto.getOperator());
		partnerProtocolRelMapper.insert(record);
		return record.getId();
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deletePartnerProtocolRel(
			PartnerProtocolRelDeleteDto partnerProtocolRelDeleteDto)
			 {
		ValidateUtils.validateParam(partnerProtocolRelDeleteDto);
		ValidateUtils.notNull(partnerProtocolRelDeleteDto.getTargetType());
		ValidateUtils.notNull(partnerProtocolRelDeleteDto.getObjectId());
		ValidateUtils.notEmpty(partnerProtocolRelDeleteDto.getProtocolTypeList());
		List<Long>  protocolIds = protocolBO.getAllProtocolId(partnerProtocolRelDeleteDto.getProtocolTypeList());
		if (protocolIds == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"protocolBO.getAllProtocolId is null");
		}
		PartnerProtocolRelExample example = new PartnerProtocolRelExample();
		
		Criteria criteria = example.createCriteria();

		criteria.andObjectIdEqualTo(partnerProtocolRelDeleteDto.getObjectId());
		criteria.andTargetTypeEqualTo(partnerProtocolRelDeleteDto.getTargetType().getCode());
		criteria.andProtocolIdIn(protocolIds);
		criteria.andIsDeletedEqualTo("n");
		
		PartnerProtocolRel  record = new PartnerProtocolRel();
		DomainUtils.beforeDelete(record, partnerProtocolRelDeleteDto.getOperator());
		
		partnerProtocolRelMapper.updateByExampleSelective(record, example);
	}

	@Override
	public PartnerProtocolRelDto getPartnerProtocolRelDto(
			ProtocolTypeEnum type, Long objectId,
			PartnerProtocolRelTargetTypeEnum targetType)
			 {
		ValidateUtils.notNull(type);
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(targetType);
		
		List<ProtocolTypeEnum> types = new ArrayList<ProtocolTypeEnum>();
		types.add(type);
		List<Long>  protocolIds = protocolBO.getAllProtocolId(types);
		if (protocolIds == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"protocolBO.getAllProtocolId is null");
		}
		
		PartnerProtocolRelExample example = new PartnerProtocolRelExample();
		
		Criteria criteria = example.createCriteria();

		criteria.andObjectIdEqualTo(objectId);
		criteria.andTargetTypeEqualTo(targetType.getCode());
		criteria.andProtocolIdIn(protocolIds);
		criteria.andIsDeletedEqualTo("n");
		example.setOrderByClause("id DESC");
		
		List<PartnerProtocolRel> res = partnerProtocolRelMapper.selectByExample(example);
		return PartnerProtocolRelConverter.toPartnerProtocolRelDto(ResultUtils.selectOne(res));
	}

	
	
	@Override
	public PartnerProtocolRelDto getPartnerProtocolRelDto(Long objectId,
			PartnerProtocolRelTargetTypeEnum targetType, Long protocolId)  {
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(targetType);
		PartnerProtocolRelExample example = new PartnerProtocolRelExample();
		
		Criteria criteria = example.createCriteria();

		criteria.andObjectIdEqualTo(objectId);
		criteria.andTargetTypeEqualTo(targetType.getCode());
		criteria.andProtocolIdEqualTo(protocolId);
		criteria.andIsDeletedEqualTo("n");
		example.setOrderByClause("id DESC");
		
		List<PartnerProtocolRel> res = partnerProtocolRelMapper.selectByExample(example);
		return PartnerProtocolRelConverter.toPartnerProtocolRelDto(ResultUtils.selectOne(res));
	}

	@Override
	public PartnerProtocolRelDto getLastPartnerProtocolRelDtoByTaobaoUserId(Long taobaoUserId, ProtocolTypeEnum type,
			PartnerProtocolRelTargetTypeEnum targetType)  {
		ValidateUtils.notNull(type);
		ValidateUtils.notNull(taobaoUserId);
		ValidateUtils.notNull(targetType);
		
		List<ProtocolTypeEnum> types = new ArrayList<ProtocolTypeEnum>();
		types.add(type);
		List<Long>  protocolIds = protocolBO.getAllProtocolId(types);
		if (protocolIds == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"protocolBO.getAllProtocolId is null");
		}
		
		PartnerProtocolRelExample example = new PartnerProtocolRelExample();
		
		Criteria criteria = example.createCriteria();

		criteria.andTaobaoUserIdEqualTo(taobaoUserId);
		criteria.andTargetTypeEqualTo(targetType.getCode());
		criteria.andProtocolIdIn(protocolIds);
		criteria.andIsDeletedEqualTo("n");
		example.setOrderByClause("id DESC");
		
		List<PartnerProtocolRel> res = partnerProtocolRelMapper.selectByExample(example);
		return PartnerProtocolRelConverter.toPartnerProtocolRelDto(ResultUtils.selectOne(res));
	}
}
