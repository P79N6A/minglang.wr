package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cuntao_flow_record")
public class CuntaoFlowRecord {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * 业务id
     */
    @Column(name = "target_id")
    private Long targetId;

    /**
     * 业务类型
     */
    @Column(name = "target_type")
    private String targetType;

    /**
     * 节点标题
     */
    @Column(name = "node_title")
    private String nodeTitle;

    /**
     * 操作人名字
     */
    @Column(name = "operator_name")
    private String operatorName;

    /**
     * 操作人工号
     */
    @Column(name = "operator_workid")
    private String operatorWorkid;

    /**
     * 操作时间
     */
    @Column(name = "operate_time")
    private Date operateTime;

    /**
     * 操作意见
     */
    @Column(name = "operate_opinion")
    private String operateOpinion;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 流程id
     */
    @Column(name = "flow_id")
    private Long flowId;

    /**
     * 异步消息id
     */
    @Column(name = "trace_id")
    private String traceId;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取修改人
     *
     * @return modifier - 修改人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 设置修改人
     *
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取业务id
     *
     * @return target_id - 业务id
     */
    public Long getTargetId() {
        return targetId;
    }

    /**
     * 设置业务id
     *
     * @param targetId 业务id
     */
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    /**
     * 获取业务类型
     *
     * @return target_type - 业务类型
     */
    public String getTargetType() {
        return targetType;
    }

    /**
     * 设置业务类型
     *
     * @param targetType 业务类型
     */
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    /**
     * 获取节点标题
     *
     * @return node_title - 节点标题
     */
    public String getNodeTitle() {
        return nodeTitle;
    }

    /**
     * 设置节点标题
     *
     * @param nodeTitle 节点标题
     */
    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    /**
     * 获取操作人名字
     *
     * @return operator_name - 操作人名字
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * 设置操作人名字
     *
     * @param operatorName 操作人名字
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * 获取操作人工号
     *
     * @return operator_workid - 操作人工号
     */
    public String getOperatorWorkid() {
        return operatorWorkid;
    }

    /**
     * 设置操作人工号
     *
     * @param operatorWorkid 操作人工号
     */
    public void setOperatorWorkid(String operatorWorkid) {
        this.operatorWorkid = operatorWorkid;
    }

    /**
     * 获取操作时间
     *
     * @return operate_time - 操作时间
     */
    public Date getOperateTime() {
        return operateTime;
    }

    /**
     * 设置操作时间
     *
     * @param operateTime 操作时间
     */
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 获取操作意见
     *
     * @return operate_opinion - 操作意见
     */
    public String getOperateOpinion() {
        return operateOpinion;
    }

    /**
     * 设置操作意见
     *
     * @param operateOpinion 操作意见
     */
    public void setOperateOpinion(String operateOpinion) {
        this.operateOpinion = operateOpinion;
    }

    /**
     * 获取备注
     *
     * @return remarks - 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取流程id
     *
     * @return flow_id - 流程id
     */
    public Long getFlowId() {
        return flowId;
    }

    /**
     * 设置流程id
     *
     * @param flowId 流程id
     */
    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    /**
     * 获取异步消息id
     *
     * @return trace_id - 异步消息id
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * 设置异步消息id
     *
     * @param traceId 异步消息id
     */
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}