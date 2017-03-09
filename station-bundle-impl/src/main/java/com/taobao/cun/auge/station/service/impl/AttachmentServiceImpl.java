package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.dto.AttachementDeleteDto;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementTypeIdEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.AttachmentService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiao on 16/12/22.
 */
@Service("attachmentService")
@HSFProvider(serviceInterface = AttachmentService.class)
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachementBO attachementBO;

    @Override
    public List<AttachementDto> getAttachmentList(Long objectId, AttachementBizTypeEnum bizTypeEnum, AttachementTypeIdEnum attachementTypeId) throws AugeServiceException {
        return attachementBO.getAttachementList(objectId, bizTypeEnum, attachementTypeId);
    }

    @Override
    public void addAttachmentBatch(List<AttachementDto> attachmentDtoList, Long objectId, AttachementBizTypeEnum bizTypeEnum, AttachementTypeIdEnum attachmentTypeId, OperatorDto operatorDto) throws AugeServiceException {
        attachementBO.addAttachementBatch(attachmentDtoList, objectId, bizTypeEnum, attachmentTypeId, operatorDto);
    }

	@Override
	public List<Long> modifyAttachementBatch(
			List<AttachementDto> attachementDtoList, Long objectId,
			AttachementBizTypeEnum bizTypeEnum, OperatorDto operatorDto)
			throws AugeServiceException {
		return attachementBO.modifyAttachementBatch(attachementDtoList, objectId, bizTypeEnum, operatorDto);
	}

	@Override
	public void modifyAttachementBatch(List<AttachementDto> attachementDtoList,
			Long objectId, AttachementBizTypeEnum bizTypeEnum,
			AttachementTypeIdEnum attachementTypeId, OperatorDto operatorDto)
			throws AugeServiceException {
		attachementBO.modifyAttachementBatch(attachementDtoList, objectId, bizTypeEnum, attachementTypeId, operatorDto);
		
	}

	@Override
	public List<Long> addAttachementBatch(
			List<AttachementDto> attachementDtoList, Long objectId,
			AttachementBizTypeEnum bizTypeEnum, OperatorDto operatorDto)
			throws AugeServiceException {
		return attachementBO.addAttachementBatch(attachementDtoList, objectId, bizTypeEnum, operatorDto);
	}

	@Override
	public List<AttachementDto> getAttachementList(Long objectId,
			AttachementBizTypeEnum bizTypeEnum) throws AugeServiceException {
		return attachementBO.getAttachementList(objectId, bizTypeEnum);
	}

	@Override
	public void deleteAttachement(AttachementDeleteDto attachementDeleteDto)
			throws AugeServiceException {
		attachementBO.deleteAttachement(attachementDeleteDto);
		
	}
}
