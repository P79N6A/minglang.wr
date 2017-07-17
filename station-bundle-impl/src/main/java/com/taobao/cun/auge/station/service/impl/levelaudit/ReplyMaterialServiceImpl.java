package com.taobao.cun.auge.station.service.impl.levelaudit;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.evaluate.dto.PartnerLevelTaskBusinessDataDTO;
import com.taobao.cun.auge.evaluate.dto.ReplyMaterialDTO;
import com.taobao.cun.auge.evaluate.enums.LevelTaskDataTypeEnum;
import com.taobao.cun.auge.evaluate.enums.TaskNodeAuditStatus;
import com.taobao.cun.auge.evaluate.service.PartnerLevelTaskBusinessDataService;
import com.taobao.cun.auge.evaluate.service.ReplyMaterialService;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * Created by xujianhui on 16/12/9.
 */
@Service("replyMaterialService")
@HSFProvider(serviceInterface = ReplyMaterialService.class)
public class ReplyMaterialServiceImpl implements ReplyMaterialService {

    private Logger logger = LoggerFactory.getLogger(ReplyMaterialServiceImpl.class);

    @Autowired
    private PartnerLevelTaskBusinessDataService partnerLevelTaskBusinessDataService;

    @Override
    public List<ReplyMaterialDTO> queryReplyMaterialDTOs(Long taobaoUserId){
        List<PartnerLevelTaskBusinessDataDTO> dataDtos = partnerLevelTaskBusinessDataService.queryByAuditedPersonId(taobaoUserId, LevelTaskDataTypeEnum.REPLY_ATTACHMENT);
        if (CollectionUtils.isEmpty(dataDtos)) {
            return Collections.emptyList();
        }
        List<ReplyMaterialDTO> materials = Lists.newArrayList();
        for (PartnerLevelTaskBusinessDataDTO dto : dataDtos) {
            ReplyMaterialDTO materialDTO = getReplyMaterialDTO(dto);
            materials.add(materialDTO);
        }
        return materials;
    }

    private ReplyMaterialDTO getReplyMaterialDTO(PartnerLevelTaskBusinessDataDTO dto) {
        ReplyMaterialDTO materialDTO = new ReplyMaterialDTO();
        setUploadStatus(dto, materialDTO, dto.getTaskBusinessInfo());
        materialDTO.setLevelTaskNodeId(dto.getTaskId());
        materialDTO.setProcessInstanceId(dto.getProcessInstanceId());
        return materialDTO;
    }

    private void setUploadStatus(PartnerLevelTaskBusinessDataDTO dto, ReplyMaterialDTO materialDTO, String attachmentStr) {
        if(TaskNodeAuditStatus.isAudited(dto.getAuditStatus())){
            materialDTO.setStatus(ReplyMaterialDTO.UploadStatus.CANT_UPLOAD.name());
        }
        if(!StringUtils.isEmpty(attachmentStr) && !CollectionUtils.isEmpty(JSON.parseArray(attachmentStr, String.class))) {
            List<String> attachmentIdentifiers = JSON.parseArray(attachmentStr, String.class);
            List<ReplyMaterialDTO.Attachment> attachmentDtoList = Lists.newArrayList();
            for (String identifier : attachmentIdentifiers) {
                ReplyMaterialDTO.Attachment tmp = new ReplyMaterialDTO.Attachment();
                tmp.setFileName(identifier);
                tmp.setUrl(identifier);
                attachmentDtoList.add(tmp);
            }
            materialDTO.setAttachmentDtoList(attachmentDtoList);
        }
    }

    @Override
    public boolean submitEvaluateAttachments(Long taobaoUserId, String processInstanceId, List<String> attachmentIdentifiers){
        Assert.notNull(taobaoUserId);
        Assert.notNull(processInstanceId);
        List<PartnerLevelTaskBusinessDataDTO> dataDtos = partnerLevelTaskBusinessDataService.queryByProcessInstanceAndTaskId(processInstanceId, null, LevelTaskDataTypeEnum.REPLY_ATTACHMENT);
        PartnerLevelTaskBusinessDataDTO dto = dataDtos.get(0);
        if (!taobaoUserId.equals(dto.getAuditedPersonId()) || TaskNodeAuditStatus.isAudited(dto.getAuditStatus())) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PRIVILEDGE_ERROR_CODE,"No Priviledge To Submit Attachements!");
        }
        PartnerLevelTaskBusinessDataDTO updateDto = new PartnerLevelTaskBusinessDataDTO();
        updateDto.setAuditedPersonId(taobaoUserId);
        updateDto.setInfoType(LevelTaskDataTypeEnum.REPLY_ATTACHMENT);
        updateDto.setProcessInstanceId(processInstanceId);
        updateDto.setId(dto.getId());
        if (!CollectionUtils.isEmpty(attachmentIdentifiers)) {
            dto.setTaskBusinessInfo(JSON.toJSONString(attachmentIdentifiers));
        }
        return partnerLevelTaskBusinessDataService.saveTaskBusinessData(processInstanceId, null, dto);
    }

    @Override
    public ReplyMaterialDTO queryReplyMateraByApproveInstanceId(String processInstanceId, Long taskId) {
        Assert.notNull(processInstanceId);
        List<PartnerLevelTaskBusinessDataDTO> dataDTOs = partnerLevelTaskBusinessDataService.queryByProcessInstanceAndTaskId(processInstanceId, taskId, LevelTaskDataTypeEnum.REPLY_ATTACHMENT);
        if (CollectionUtils.isEmpty(dataDTOs)) {
            return null;
        }

        PartnerLevelTaskBusinessDataDTO dto = dataDTOs.get(0);
        return getReplyMaterialDTO(dto);
    }
}
