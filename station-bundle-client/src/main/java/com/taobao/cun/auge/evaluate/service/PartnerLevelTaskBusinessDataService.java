package com.taobao.cun.auge.evaluate.service;

import com.taobao.cun.auge.evaluate.dto.PartnerLevelTaskBusinessDataDTO;
import com.taobao.cun.auge.evaluate.enums.LevelTaskDataTypeEnum;

import java.util.List;

/**
 * 晋升任务业务数据表
 * Created by xujianhui on 16/12/13.
 */
public interface PartnerLevelTaskBusinessDataService {
    /**
     * 保存业务数据
     * @param processInstanceId 流程实例Id approver_process_instance中的instance_id
     * @param taskId task_flow表中的task_id
     * @param businessDataDTO 业务数据
     * @return
     */
    boolean saveTaskBusinessData(Long processInstanceId, Long taskId, PartnerLevelTaskBusinessDataDTO businessDataDTO);

    /**
     * 查询某个被审批人员的所有某种类型的任务数据;
     * @param auditedPersonId
     * @param dataType
     * @return
     */
    List<PartnerLevelTaskBusinessDataDTO> queryByAuditedPersonId(Long auditedPersonId, LevelTaskDataTypeEnum dataType);

    /**
     * 查看某个任务流程节点的任务数据
     * @param processInstanceId
     * @param taskId
     * @param dataType
     * @return
     */
    List<PartnerLevelTaskBusinessDataDTO> queryByProcessInstanceAndTaskId(Long processInstanceId, Long taskId, LevelTaskDataTypeEnum dataType);

}
