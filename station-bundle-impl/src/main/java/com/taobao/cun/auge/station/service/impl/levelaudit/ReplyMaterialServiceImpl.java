package com.taobao.cun.auge.station.service.impl.levelaudit;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.evaluate.dto.ReplyMaterialDTO;
import com.taobao.cun.auge.evaluate.service.ReplyMaterialService;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xujianhui on 16/12/9.
 */
@Service("replyMaterialService")
@HSFProvider(serviceInterface = ReplyMaterialService.class)
public class ReplyMaterialServiceImpl implements ReplyMaterialService {

    @Override
    public List<ReplyMaterialDTO> queryReplyMaterialDTOs(Long taobaoUserId) throws AugeServiceException {
        List<ReplyMaterialDTO> materials = Lists.newArrayList();
        ReplyMaterialDTO dto = new ReplyMaterialDTO();
        dto.setLevelTaskNodeId(1234L);
        dto.setReplayEventDesc("EVALUATE|S7");
        dto.setStatus("NOTSUBMIT");
        List<ReplyMaterialDTO.Attachment> attachmentDtoList = Lists.newArrayList();
        ReplyMaterialDTO.Attachment attachment = new ReplyMaterialDTO.Attachment();
        attachment.setFileName("28799的S7晋升答辩材料.rar");
        attachment.setFileType("rar");
        attachment.setUrl("http://osss.fdafsdf.fsdfds.com/fsdfds");
        attachmentDtoList.add(attachment);
        dto.setAttachmentDtoList(attachmentDtoList);
        materials.add(dto);
        return materials;
    }

    @Override
    public boolean submitEvaluateAttachments(Long taobaoUserId, Long levelTaskNodeId, List<String> attachmentIdentifiers) throws AugeServiceException {
        return true;
    }
}
