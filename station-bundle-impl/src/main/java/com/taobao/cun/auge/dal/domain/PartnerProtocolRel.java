package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "partner_protocol_rel")
public class PartnerProtocolRel {
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
     * 是否已删除
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * 合伙人淘宝user_id
     */
    @Column(name = "taobao_user_id")
    private Long taobaoUserId;

    /**
     * 协议表主键
     */
    @Column(name = "protocol_id")
    private Long protocolId;

    /**
     * 合伙人确认协议时间
     */
    @Column(name = "confirm_time")
    private Date confirmTime;

    /**
     * 协议生效时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 协议失效时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 业务表主键id
     */
    @Column(name = "object_id")
    private Long objectId;

    /**
     * 业务类型
     */
    @Column(name = "target_type")
    private String targetType;

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
     * 获取是否已删除
     *
     * @return is_deleted - 是否已删除
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否已删除
     *
     * @param isDeleted 是否已删除
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取合伙人淘宝user_id
     *
     * @return taobao_user_id - 合伙人淘宝user_id
     */
    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    /**
     * 设置合伙人淘宝user_id
     *
     * @param taobaoUserId 合伙人淘宝user_id
     */
    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    /**
     * 获取协议表主键
     *
     * @return protocol_id - 协议表主键
     */
    public Long getProtocolId() {
        return protocolId;
    }

    /**
     * 设置协议表主键
     *
     * @param protocolId 协议表主键
     */
    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    /**
     * 获取合伙人确认协议时间
     *
     * @return confirm_time - 合伙人确认协议时间
     */
    public Date getConfirmTime() {
        return confirmTime;
    }

    /**
     * 设置合伙人确认协议时间
     *
     * @param confirmTime 合伙人确认协议时间
     */
    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    /**
     * 获取协议生效时间
     *
     * @return start_time - 协议生效时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置协议生效时间
     *
     * @param startTime 协议生效时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取协议失效时间
     *
     * @return end_time - 协议失效时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置协议失效时间
     *
     * @param endTime 协议失效时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取业务表主键id
     *
     * @return object_id - 业务表主键id
     */
    public Long getObjectId() {
        return objectId;
    }

    /**
     * 设置业务表主键id
     *
     * @param objectId 业务表主键id
     */
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
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
}