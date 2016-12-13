package com.taobao.cun.auge.evaluate.dto;

import com.taobao.cun.auge.evaluate.enums.LevelTaskDataTypeEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 评级任务节点的业务数据
 * Created by xujianhui on 16/12/13.
 */
public class PartnerLevelTaskBusinessDataDTO implements Serializable {

    private static final long serialVersionUID = -8884912842266054508L;

    private Long id;

    /**
     * 被审批人
     */
    @NotNull(message = "auditedPersonId is null")
    private Long auditedPersonId;

    @NotNull(message = "porocessInstanceId is null")
    private Long processInstanceId;

    private Long taskId;

    /**
     * 审批人
     */
    private Long auditPersonId;

    /**
     * 参与人员
     */
    private String participantsId;

    /**
     * 数据类型如:答辩附件材料
     */
    @NotNull(message = "infoType is null!")
    private String infoType;

    /**
     * 具体数据
     */
    private String taskBusinessInfo;

    /**
     * 扩展信息
     */
    private String extendsInfo;

    /**
     * 审批状态
     */
    private String auditStatus;

    public Long getAuditedPersonId() {
        return auditedPersonId;
    }

    public void setAuditedPersonId(Long auditedPersonId) {
        this.auditedPersonId = auditedPersonId;
    }

    public Long getAuditPersonId() {
        return auditPersonId;
    }

    public void setAuditPersonId(Long auditPersonId) {
        this.auditPersonId = auditPersonId;
    }

    public String getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(String participantsId) {
        this.participantsId = participantsId;
    }

    public LevelTaskDataTypeEnum getInfoType() {
        return LevelTaskDataTypeEnum.valueOf(infoType);
    }

    public void setInfoType(LevelTaskDataTypeEnum infoType) {
        this.infoType = infoType.name();
    }

    public String getTaskBusinessInfo() {
        return taskBusinessInfo;
    }

    public void setTaskBusinessInfo(String taskBusinessInfo) {
        this.taskBusinessInfo = taskBusinessInfo;
    }

    public String getExtendsInfo() {
        return extendsInfo;
    }

    public void setExtendsInfo(String extendsInfo) {
        this.extendsInfo = extendsInfo;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
