package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "partner_station_rel")
public class PartnerStationRel {
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
     * 创建者
     */
    private String creator;

    /**
     * 修改者
     */
    private String modifier;

    /**
     * 是否已删除
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * 村服务站id
     */
    @Column(name = "station_id")
    private Long stationId;

    /**
     * 申请时间
     */
    @Column(name = "apply_time")
    private Date applyTime;

    /**
     * 服务开始时间
     */
    @Column(name = "service_begin_time")
    private Date serviceBeginTime;

    /**
     * 服务结束时间
     */
    @Column(name = "service_end_time")
    private Date serviceEndTime;

    /**
     * 合伙人id
     */
    @Column(name = "partner_id")
    private Long partnerId;

    /**
     * 淘帮手所属合伙人的村服务站id
     */
    @Column(name = "parent_station_id")
    private Long parentStationId;

    /**
     * 状态
     */
    private String state;

    /**
     * 申请人
     */
    @Column(name = "applier_id")
    private String applierId;

    /**
     * 位：可以用来标示淘帮手是否是由合伙人降级而来
     */
    private Integer bit;

    /**
     * 合伙人or村拍档
     */
    private String type;

    /**
     * 开业时间
     */
    @Column(name = "open_date")
    private Date openDate;

    /**
     * 是否是当前人
     */
    @Column(name = "is_current")
    private String isCurrent;

    /**
     * 申请人类型，buc，还是havana
     */
    @Column(name = "applier_type")
    private String applierType;

    /**
     * 停业类型：合伙人主动退出，还是小二主动清退
     */
    @Column(name = "close_type")
    private String closeType;

    /**
     * station_aply表主键，供数据迁移使用
     */
    @Column(name = "station_apply_id")
    private Long stationApplyId;

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
     * 获取创建者
     *
     * @return creator - 创建者
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建者
     *
     * @param creator 创建者
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取修改者
     *
     * @return modifier - 修改者
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 设置修改者
     *
     * @param modifier 修改者
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
     * 获取村服务站id
     *
     * @return station_id - 村服务站id
     */
    public Long getStationId() {
        return stationId;
    }

    /**
     * 设置村服务站id
     *
     * @param stationId 村服务站id
     */
    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    /**
     * 获取申请时间
     *
     * @return apply_time - 申请时间
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * 设置申请时间
     *
     * @param applyTime 申请时间
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * 获取服务开始时间
     *
     * @return service_begin_time - 服务开始时间
     */
    public Date getServiceBeginTime() {
        return serviceBeginTime;
    }

    /**
     * 设置服务开始时间
     *
     * @param serviceBeginTime 服务开始时间
     */
    public void setServiceBeginTime(Date serviceBeginTime) {
        this.serviceBeginTime = serviceBeginTime;
    }

    /**
     * 获取服务结束时间
     *
     * @return service_end_time - 服务结束时间
     */
    public Date getServiceEndTime() {
        return serviceEndTime;
    }

    /**
     * 设置服务结束时间
     *
     * @param serviceEndTime 服务结束时间
     */
    public void setServiceEndTime(Date serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    /**
     * 获取合伙人id
     *
     * @return partner_id - 合伙人id
     */
    public Long getPartnerId() {
        return partnerId;
    }

    /**
     * 设置合伙人id
     *
     * @param partnerId 合伙人id
     */
    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    /**
     * 获取淘帮手所属合伙人的村服务站id
     *
     * @return parent_station_id - 淘帮手所属合伙人的村服务站id
     */
    public Long getParentStationId() {
        return parentStationId;
    }

    /**
     * 设置淘帮手所属合伙人的村服务站id
     *
     * @param parentStationId 淘帮手所属合伙人的村服务站id
     */
    public void setParentStationId(Long parentStationId) {
        this.parentStationId = parentStationId;
    }

    /**
     * 获取状态
     *
     * @return state - 状态
     */
    public String getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state 状态
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取申请人
     *
     * @return applier_id - 申请人
     */
    public String getApplierId() {
        return applierId;
    }

    /**
     * 设置申请人
     *
     * @param applierId 申请人
     */
    public void setApplierId(String applierId) {
        this.applierId = applierId;
    }

    /**
     * 获取位：可以用来标示淘帮手是否是由合伙人降级而来
     *
     * @return bit - 位：可以用来标示淘帮手是否是由合伙人降级而来
     */
    public Integer getBit() {
        return bit;
    }

    /**
     * 设置位：可以用来标示淘帮手是否是由合伙人降级而来
     *
     * @param bit 位：可以用来标示淘帮手是否是由合伙人降级而来
     */
    public void setBit(Integer bit) {
        this.bit = bit;
    }

    /**
     * 获取合伙人or村拍档
     *
     * @return type - 合伙人or村拍档
     */
    public String getType() {
        return type;
    }

    /**
     * 设置合伙人or村拍档
     *
     * @param type 合伙人or村拍档
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取开业时间
     *
     * @return open_date - 开业时间
     */
    public Date getOpenDate() {
        return openDate;
    }

    /**
     * 设置开业时间
     *
     * @param openDate 开业时间
     */
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    /**
     * 获取是否是当前人
     *
     * @return is_current - 是否是当前人
     */
    public String getIsCurrent() {
        return isCurrent;
    }

    /**
     * 设置是否是当前人
     *
     * @param isCurrent 是否是当前人
     */
    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }

    /**
     * 获取申请人类型，buc，还是havana
     *
     * @return applier_type - 申请人类型，buc，还是havana
     */
    public String getApplierType() {
        return applierType;
    }

    /**
     * 设置申请人类型，buc，还是havana
     *
     * @param applierType 申请人类型，buc，还是havana
     */
    public void setApplierType(String applierType) {
        this.applierType = applierType;
    }

    /**
     * 获取停业类型：合伙人主动退出，还是小二主动清退
     *
     * @return close_type - 停业类型：合伙人主动退出，还是小二主动清退
     */
    public String getCloseType() {
        return closeType;
    }

    /**
     * 设置停业类型：合伙人主动退出，还是小二主动清退
     *
     * @param closeType 停业类型：合伙人主动退出，还是小二主动清退
     */
    public void setCloseType(String closeType) {
        this.closeType = closeType;
    }

    /**
     * 获取station_aply表主键，供数据迁移使用
     *
     * @return station_apply_id - station_aply表主键，供数据迁移使用
     */
    public Long getStationApplyId() {
        return stationApplyId;
    }

    /**
     * 设置station_aply表主键，供数据迁移使用
     *
     * @param stationApplyId station_aply表主键，供数据迁移使用
     */
    public void setStationApplyId(Long stationApplyId) {
        this.stationApplyId = stationApplyId;
    }
}