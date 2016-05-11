package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "partner_lifecycle_items")
public class PartnerLifecycleItems {
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
     * 合伙人类型
     */
    @Column(name = "partner_type")
    private String partnerType;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * 入驻协议签署标签
     */
    @Column(name = "settled_protocol")
    private String settledProtocol;

    /**
     * 保证金标签
     */
    private String bond;

    /**
     * 退出协议标签
     */
    @Column(name = "quit_protocol")
    private String quitProtocol;

    /**
     * 物流审批
     */
    @Column(name = "logistics_approve")
    private String logisticsApprove;

    /**
     * 合伙人实例的主键
     */
    @Column(name = "partner_instance_id")
    private Long partnerInstanceId;

    /**
     * 是否已经删除
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * 同一业务类型中，当前操作步骤
     */
    @Column(name = "current_step")
    private String currentStep;

    /**
     * 角色审批
     */
    @Column(name = "role_approve")
    private String roleApprove;

    /**
     * 小二确认
     */
    private String confirm;

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
     * 获取合伙人类型
     *
     * @return partner_type - 合伙人类型
     */
    public String getPartnerType() {
        return partnerType;
    }

    /**
     * 设置合伙人类型
     *
     * @param partnerType 合伙人类型
     */
    public void setPartnerType(String partnerType) {
        this.partnerType = partnerType;
    }

    /**
     * 获取业务类型
     *
     * @return business_type - 业务类型
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * 设置业务类型
     *
     * @param businessType 业务类型
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * 获取入驻协议签署标签
     *
     * @return settled_protocol - 入驻协议签署标签
     */
    public String getSettledProtocol() {
        return settledProtocol;
    }

    /**
     * 设置入驻协议签署标签
     *
     * @param settledProtocol 入驻协议签署标签
     */
    public void setSettledProtocol(String settledProtocol) {
        this.settledProtocol = settledProtocol;
    }

    /**
     * 获取保证金标签
     *
     * @return bond - 保证金标签
     */
    public String getBond() {
        return bond;
    }

    /**
     * 设置保证金标签
     *
     * @param bond 保证金标签
     */
    public void setBond(String bond) {
        this.bond = bond;
    }

    /**
     * 获取退出协议标签
     *
     * @return quit_protocol - 退出协议标签
     */
    public String getQuitProtocol() {
        return quitProtocol;
    }

    /**
     * 设置退出协议标签
     *
     * @param quitProtocol 退出协议标签
     */
    public void setQuitProtocol(String quitProtocol) {
        this.quitProtocol = quitProtocol;
    }

    /**
     * 获取物流审批
     *
     * @return logistics_approve - 物流审批
     */
    public String getLogisticsApprove() {
        return logisticsApprove;
    }

    /**
     * 设置物流审批
     *
     * @param logisticsApprove 物流审批
     */
    public void setLogisticsApprove(String logisticsApprove) {
        this.logisticsApprove = logisticsApprove;
    }

    /**
     * 获取合伙人实例的主键
     *
     * @return partner_instance_id - 合伙人实例的主键
     */
    public Long getPartnerInstanceId() {
        return partnerInstanceId;
    }

    /**
     * 设置合伙人实例的主键
     *
     * @param partnerInstanceId 合伙人实例的主键
     */
    public void setPartnerInstanceId(Long partnerInstanceId) {
        this.partnerInstanceId = partnerInstanceId;
    }

    /**
     * 获取是否已经删除
     *
     * @return is_deleted - 是否已经删除
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否已经删除
     *
     * @param isDeleted 是否已经删除
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取同一业务类型中，当前操作步骤
     *
     * @return current_step - 同一业务类型中，当前操作步骤
     */
    public String getCurrentStep() {
        return currentStep;
    }

    /**
     * 设置同一业务类型中，当前操作步骤
     *
     * @param currentStep 同一业务类型中，当前操作步骤
     */
    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    /**
     * 获取角色审批
     *
     * @return role_approve - 角色审批
     */
    public String getRoleApprove() {
        return roleApprove;
    }

    /**
     * 设置角色审批
     *
     * @param roleApprove 角色审批
     */
    public void setRoleApprove(String roleApprove) {
        this.roleApprove = roleApprove;
    }

    /**
     * 获取小二确认
     *
     * @return confirm - 小二确认
     */
    public String getConfirm() {
        return confirm;
    }

    /**
     * 设置小二确认
     *
     * @param confirm 小二确认
     */
    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}