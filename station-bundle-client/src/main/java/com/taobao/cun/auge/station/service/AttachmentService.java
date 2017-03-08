package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.dto.AttachementDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementTypeIdEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

import java.util.List;

/**
 * Created by xiao on 16/12/22.
 */
public interface AttachmentService {

    public List<AttachementDto> getAttachmentList(Long objectId, AttachementBizTypeEnum bizTypeEnum, AttachementTypeIdEnum attachementTypeId) throws AugeServiceException;

    public void addAttachmentBatch(List<AttachementDto> attachmentDtoList,Long objectId,AttachementBizTypeEnum bizTypeEnum, AttachementTypeIdEnum attachmentTypeId,OperatorDto operatorDto) throws AugeServiceException;
}
