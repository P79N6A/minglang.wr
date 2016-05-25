package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Attachement;
import com.taobao.cun.auge.dal.mapper.AttachementMapper;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.dto.AttachementDeleteDto;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("attachementBO")
public class AttachementBOImpl implements AttachementBO {
	
	@Autowired
	AttachementMapper attachementMapper;
	@Override
	public Long addAttachement(AttachementDto attachementDto)
			throws AugeServiceException {
		ValidateUtils.notNull(attachementDto);
		Attachement attachement = convertToDomain(attachementDto);
		DomainUtils.beforeInsert(attachement, attachementDto.getOperator());
		attachementMapper.insert(attachement);
		return attachement.getId();
	}

	@Override
	public Long deleteAttachement(AttachementDeleteDto attachementDeleteDto)
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
		return null;
	}
	
	private Attachement convertToDomain(AttachementDto attachementDto) {
		Attachement attachement = new Attachement();
		attachement.setAttachementTypeId(attachementDto.getAttachementTypeId().getCode());
		attachement.setBizType(attachementDto.getBizType().getCode());
		attachement.setDescription(attachementDto.getDescription());
		attachement.setFileType(attachementDto.getFileType());
		attachement.setFsId(attachementDto.getFsId());
		attachement.setObjectId(attachementDto.getObjectId());
		attachement.setTitle(attachementDto.getTitle());
		return attachement;
	}

}
