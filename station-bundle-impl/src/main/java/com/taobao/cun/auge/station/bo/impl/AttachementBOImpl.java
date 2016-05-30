package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Attachement;
import com.taobao.cun.auge.dal.mapper.AttachementMapper;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.convert.AttachementConverter;
import com.taobao.cun.auge.station.dto.AttachementDeleteDto;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
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
		
		Example example = new Example(Attachement.class);
		Criteria criteria = example.createCriteria();
		criteria.andCondition("isDeleted","n");
		criteria.andCondition("objectId",attachementDeleteDto.getObjectId());
		criteria.andCondition("bizType",attachementDeleteDto.getBizType().getCode());
		if (attachementDeleteDto.getAttachementTypeId() != null) {
			criteria.andCondition("attachementTypeId", attachementDeleteDto.getAttachementTypeId().getCode());
		}
		attachementMapper.updateByExampleSelective(attachement, example);
	}
	

	@Override
	public void addAttachementBatch(List<AttachementDto> attachementDtoList,
			Long objectId, AttachementBizTypeEnum bizTypeEnum)
			throws AugeServiceException {
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(bizTypeEnum);
		if (CollectionUtils.isNotEmpty(attachementDtoList)) {
			for (AttachementDto attachementDto:attachementDtoList) {
				attachementDto.setObjectId(objectId);
				attachementDto.setBizType(bizTypeEnum);
				addAttachement(attachementDto);
			}
		}
	}

	@Override
	public void modifyAttachementBatch(List<AttachementDto> attachementDtoList,
			Long objectId, AttachementBizTypeEnum bizTypeEnum)
			throws AugeServiceException {
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(bizTypeEnum);
		AttachementDeleteDto attachementDeleteDto = new AttachementDeleteDto();
		attachementDeleteDto.setObjectId(objectId);
		attachementDeleteDto.setBizType(bizTypeEnum);
		deleteAttachement(attachementDeleteDto);
		
		addAttachementBatch(attachementDtoList, objectId, bizTypeEnum);
	}

	@Override
	public List<AttachementDto> selectAttachementList(Long objectId,
			AttachementBizTypeEnum bizTypeEnum) throws AugeServiceException {
		ValidateUtils.notNull(objectId);
		ValidateUtils.notNull(bizTypeEnum);
		Attachement record = new Attachement();
		record.setBizType(bizTypeEnum.getCode());
		record.setObjectId(objectId);
		List<Attachement> attList = attachementMapper.select(record);
		return AttachementConverter.toAttachementDtos(attList);
	}
}
