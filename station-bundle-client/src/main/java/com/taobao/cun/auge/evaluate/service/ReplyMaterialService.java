package com.taobao.cun.auge.evaluate.service;

import java.util.List;

import com.taobao.cun.auge.evaluate.dto.ReplyMaterialDTO;

/**
 * 答辩材料服务
 * Created by xujianhui on 16/12/8.
 */
public interface ReplyMaterialService {

    /**
     * 查询答辩材料
     */
    List<ReplyMaterialDTO> queryReplyMaterialDTOs(Long taobaoUserId);

    /**
     * 提交答辩材料
     * 只有审批之前可以提交材料
     */
    boolean submitEvaluateAttachments(Long taobaoUserId, String levelTaskNodeId, List<String> attachmentIdentifiers);


    /**
     * 查询审批流程中的业务数据
     * @param processInstanceId
     * @return
     */
    ReplyMaterialDTO queryReplyMateraByApproveInstanceId(String processInstanceId, Long taskId);


}
