package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRel;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExample;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerProtocolRelMapper;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.convert.PartnerProtocolRelConverter;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

@Component("partnerProtocolRelBO")
public class PartnerProtocolRelBOImpl implements PartnerProtocolRelBO {

	@Autowired
	PartnerProtocolRelMapper partnerProtocolRelMapper;

	@Autowired
	ProtocolBO protocolBO;

	@Override
	public void signProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId, PartnerProtocolRelTargetTypeEnum targetType) {
		Date now = new Date();
		signProtocol(businessId, taobaoUserId, type, now, now, null, String.valueOf(taobaoUserId),
				PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
	}

	private void signProtocol(Long objectId, Long taobaoUserId, ProtocolTypeEnum type, Date confirmTime, Date startTime, Date endTime,
			String operator, PartnerProtocolRelTargetTypeEnum targetType) {
		Long protocolId = protocolBO.getValidProtocolId(type);
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

	@Override
	public void cancelProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId, PartnerProtocolRelTargetTypeEnum targetType,
			String operator) {
		PartnerProtocolRelExample example = new PartnerProtocolRelExample();

		Criteria criteria = example.createCriteria();

		criteria.andTaobaoUserIdEqualTo(taobaoUserId);
		criteria.andTargetTypeEqualTo(targetType.getCode());
		criteria.andObjectIdEqualTo(businessId);

		Long protocolId = protocolBO.getValidProtocolId(type);
		criteria.andProtocolIdEqualTo(protocolId);

		DomainUtils.beforeDelete(example, operator);

		partnerProtocolRelMapper.deleteByExample(example);
	}

	@Override
	public Long addPartnerProtocolRel(
			PartnerProtocolRelDto partnerProtocolRelDto)
			throws AugeServiceException {
		ValidateUtils.notNull(partnerProtocolRelDto);
		PartnerProtocolRel record = PartnerProtocolRelConverter.toPartnerProtocolRel(partnerProtocolRelDto);
		DomainUtils.beforeInsert(record, partnerProtocolRelDto.getOperator());
		partnerProtocolRelMapper.insert(record);
		return record.getId();
		
	}

	@Override
	public void deletePartnerProtocolRel(
			PartnerProtocolRelDeleteDto partnerProtocolRelDeleteDto)
			throws AugeServiceException {
		ValidateUtils.validateParam(partnerProtocolRelDeleteDto);
		ValidateUtils.notNull(partnerProtocolRelDeleteDto.getTargetType());
		ValidateUtils.notNull(partnerProtocolRelDeleteDto.getObjectId());
		ValidateUtils.notEmpty(partnerProtocolRelDeleteDto.getProtocolTypeList());
		List<Long>  protocolIds = protocolBO.getAllProtocolId(partnerProtocolRelDeleteDto.getProtocolTypeList());
		if (protocolIds == null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		PartnerProtocolRelExample example = new PartnerProtocolRelExample();
		
		Criteria criteria = example.createCriteria();

		criteria.andObjectIdEqualTo(partnerProtocolRelDeleteDto.getObjectId());
		criteria.andTargetTypeEqualTo(partnerProtocolRelDeleteDto.getTargetType().getCode());
		criteria.andProtocolIdIn(protocolIds);
		
		PartnerProtocolRel  record = new PartnerProtocolRel();
		DomainUtils.beforeDelete(record, partnerProtocolRelDeleteDto.getOperator());
		
		partnerProtocolRelMapper.updateByExampleSelective(record, example);
	}

	@Override
	public PartnerProtocolRelDto getPartnerProtocolRelDto(
			ProtocolTypeEnum type, Long objectId,
			PartnerProtocolRelTargetTypeEnum targetType)
			throws AugeServiceException {
		ValidateUtils.notNull(type);
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(targetType);
		
		List<ProtocolTypeEnum> types = new ArrayList<ProtocolTypeEnum>();
		types.add(type);
		List<Long>  protocolIds = protocolBO.getAllProtocolId(types);
		if (protocolIds == null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		
		PartnerProtocolRelExample example = new PartnerProtocolRelExample();
		
		Criteria criteria = example.createCriteria();

		criteria.andObjectIdEqualTo(objectId);
		criteria.andTargetTypeEqualTo(targetType.getCode());
		criteria.andProtocolIdIn(protocolIds);
		return null;
	}
}
