package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;







import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Attachement;
import com.taobao.cun.auge.dal.domain.AttachementExample;
import com.taobao.cun.auge.dal.domain.AttachementExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AttachementMapper;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.convert.AttachementConverter;
import com.taobao.cun.auge.station.dto.AttachementDeleteDto;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementTypeIdEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("attachementBO")
public class AttachementBOImpl implements AttachementBO {
	
	@Autowired
	AttachementMapper attachementMapper;
	@Override
	public Long addAttachement(AttachementDto attachementDto)
			throws AugeServiceException {
		ValidateUtils.notNull(attachementDto);
		Attachement attachement = AttachementConverter.toAttachement(attachementDto);
		DomainUtils.beforeInsert(attachement, attachementDto.getOperator());
		attachementMapper.insert(attachement);
		return attachement.getId();
	}

	@Override
	public void deleteAttachement(AttachementDeleteDto attachementDeleteDto)
			throws AugeServiceException {
		ValidateUtils.notNull(attachementDeleteDto);
		ValidateUtils.notNull(attachementDeleteDto.getObjectId());
		ValidateUtils.notNull(attachementDeleteDto.getBizType());
		
		Attachement attachement = new Attachement();
		DomainUtils.beforeDelete(attachement, attachementDeleteDto.getOperator());
		
		AttachementExample example = new AttachementExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andObjectIdEqualTo(attachementDeleteDto.getObjectId());
		criteria.andBizTypeEqualTo(attachementDeleteDto.getBizType().getCode());

		if (attachementDeleteDto.getAttachementTypeId() != null) {
			criteria.andAttachementTypeIdEqualTo(attachementDeleteDto.getAttachementTypeId().getCode());
		}
		attachementMapper.updateByExampleSelective(attachement, example);
	}
	

	@Override
	public void addAttachementBatch(List<AttachementDto> attachementDtoList,
			Long objectId, AttachementBizTypeEnum bizTypeEnum,String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(bizTypeEnum);
		if (CollectionUtils.isNotEmpty(attachementDtoList)) {
			for (AttachementDto attachementDto:attachementDtoList) {
				attachementDto.setObjectId(objectId);
				attachementDto.setBizType(bizTypeEnum);
				attachementDto.setOperator(operator);
				addAttachement(attachementDto);
			}
		}
	}

	@Override
	public void modifyAttachementBatch(List<AttachementDto> attachementDtoList,
			Long objectId, AttachementBizTypeEnum bizTypeEnum,String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(bizTypeEnum);
		AttachementDeleteDto attachementDeleteDto = new AttachementDeleteDto();
		attachementDeleteDto.setObjectId(objectId);
		attachementDeleteDto.setBizType(bizTypeEnum);
		attachementDeleteDto.setOperator(operator);
		deleteAttachement(attachementDeleteDto);
		
		addAttachementBatch(attachementDtoList, objectId, bizTypeEnum,operator);
	}

	@Override
	public List<AttachementDto> selectAttachementList(Long objectId,
			AttachementBizTypeEnum bizTypeEnum) throws AugeServiceException {
		return selectAttachementList(objectId, bizTypeEnum, null);
	}

	@Override
	public List<AttachementDto> selectAttachementList(Long objectId,
			AttachementBizTypeEnum bizTypeEnum,
			AttachementTypeIdEnum attachementTypeId)
			throws AugeServiceException {
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(bizTypeEnum);

		AttachementExample example = new AttachementExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andObjectIdEqualTo(objectId);
		criteria.andBizTypeEqualTo(bizTypeEnum.getCode());
		if (attachementTypeId != null) {
			criteria.andAttachementTypeIdEqualTo(attachementTypeId.getCode());
		}
		List<Attachement> attList = attachementMapper.selectByExample(example);
		return AttachementConverter.toAttachementDtos(attList);
	}

	@Override
	public void addAttachementBatch(List<AttachementDto> attachementDtoList,
			Long objectId, AttachementBizTypeEnum bizTypeEnum,
			AttachementTypeIdEnum attachementTypeId, String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(bizTypeEnum);
		ValidateUtils.notNull(attachementTypeId);
		if (CollectionUtils.isNotEmpty(attachementDtoList)) {
			for (AttachementDto attachementDto:attachementDtoList) {
				attachementDto.setObjectId(objectId);
				attachementDto.setBizType(bizTypeEnum);
				attachementDto.setAttachementTypeId(attachementTypeId);
				attachementDto.setOperator(operator);
				addAttachement(attachementDto);
			}
		}
		
	}

	@Override
	public void modifyAttachementBatch(List<AttachementDto> attachementDtoList,
			Long objectId, AttachementBizTypeEnum bizTypeEnum,
			AttachementTypeIdEnum attachementTypeId, String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(bizTypeEnum);
		ValidateUtils.notNull(attachementTypeId);
		AttachementDeleteDto attachementDeleteDto = new AttachementDeleteDto();
		attachementDeleteDto.setObjectId(objectId);
		attachementDeleteDto.setBizType(bizTypeEnum);
		attachementDeleteDto.setOperator(operator);
		attachementDeleteDto.setAttachementTypeId(attachementTypeId);
		deleteAttachement(attachementDeleteDto);
		
		addAttachementBatch(attachementDtoList, objectId, bizTypeEnum,attachementTypeId,operator);
		
	}
}
