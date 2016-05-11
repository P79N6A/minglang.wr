package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "quit_station_apply")
public class QuitStationApply {
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
     * 是否删除
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 其他说明
     */
    @Column(name = "other_description")
    private String otherDescription;

    /**
     * 退出申请状态
     */
    private String state;

    /**
     * 申请单id
     */
    @Column(name = "station_apply_id")
    private Long stationApplyId;

    /**
     * 村淘撤点申请表附件名
     */
    @Column(name = "revocation_app_form_file_name")
    private String revocationAppFormFileName;

    /**
     * 贷款结清证明附件名
     */
    @Column(name = "loan_prove_file_name")
    private String loanProveFileName;

    /**
     * 审批证明附件名
     */
    @Column(name = "approval_file_name")
    private String approvalFileName;

    /**
     * 提交人姓名
     */
    @Column(name = "submitted_people_name")
    private String submittedPeopleName;

    /**
     * 资产情况
     */
    @Column(name = "asset_type")
    private String assetType;

    /**
     * 贷款是否结清
     */
    @Column(name = "loan_has_close")
    private String loanHasClose;

    /**
     * 合伙人实例id
     */
    @Column(name = "partner_instance_id")
    private Long partnerInstanceId;

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
     * 获取其他说明
     *
     * @return other_description - 其他说明
     */
    public String getOtherDescription() {
        return otherDescription;
    }

    /**
     * 设置其他说明
     *
     * @param otherDescription 其他说明
     */
    public void setOtherDescription(String otherDescription) {
        this.otherDescription = otherDescription;
    }

    /**
     * 获取退出申请状态
     *
     * @return state - 退出申请状态
     */
    public String getState() {
        return state;
    }

    /**
     * 设置退出申请状态
     *
     * @param state 退出申请状态
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取申请单id
     *
     * @return station_apply_id - 申请单id
     */
    public Long getStationApplyId() {
        return stationApplyId;
    }

    /**
     * 设置申请单id
     *
     * @param stationApplyId 申请单id
     */
    public void setStationApplyId(Long stationApplyId) {
        this.stationApplyId = stationApplyId;
    }

    /**
     * 获取村淘撤点申请表附件名
     *
     * @return revocation_app_form_file_name - 村淘撤点申请表附件名
     */
    public String getRevocationAppFormFileName() {
        return revocationAppFormFileName;
    }

    /**
     * 设置村淘撤点申请表附件名
     *
     * @param revocationAppFormFileName 村淘撤点申请表附件名
     */
    public void setRevocationAppFormFileName(String revocationAppFormFileName) {
        this.revocationAppFormFileName = revocationAppFormFileName;
    }

    /**
     * 获取贷款结清证明附件名
     *
     * @return loan_prove_file_name - 贷款结清证明附件名
     */
    public String getLoanProveFileName() {
        return loanProveFileName;
    }

    /**
     * 设置贷款结清证明附件名
     *
     * @param loanProveFileName 贷款结清证明附件名
     */
    public void setLoanProveFileName(String loanProveFileName) {
        this.loanProveFileName = loanProveFileName;
    }

    /**
     * 获取审批证明附件名
     *
     * @return approval_file_name - 审批证明附件名
     */
    public String getApprovalFileName() {
        return approvalFileName;
    }

    /**
     * 设置审批证明附件名
     *
     * @param approvalFileName 审批证明附件名
     */
    public void setApprovalFileName(String approvalFileName) {
        this.approvalFileName = approvalFileName;
    }

    /**
     * 获取提交人姓名
     *
     * @return submitted_people_name - 提交人姓名
     */
    public String getSubmittedPeopleName() {
        return submittedPeopleName;
    }

    /**
     * 设置提交人姓名
     *
     * @param submittedPeopleName 提交人姓名
     */
    public void setSubmittedPeopleName(String submittedPeopleName) {
        this.submittedPeopleName = submittedPeopleName;
    }

    /**
     * 获取资产情况
     *
     * @return asset_type - 资产情况
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * 设置资产情况
     *
     * @param assetType 资产情况
     */
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    /**
     * 获取贷款是否结清
     *
     * @return loan_has_close - 贷款是否结清
     */
    public String getLoanHasClose() {
        return loanHasClose;
    }

    /**
     * 设置贷款是否结清
     *
     * @param loanHasClose 贷款是否结清
     */
    public void setLoanHasClose(String loanHasClose) {
        this.loanHasClose = loanHasClose;
    }

    /**
     * 获取合伙人实例id
     *
     * @return partner_instance_id - 合伙人实例id
     */
    public Long getPartnerInstanceId() {
        return partnerInstanceId;
    }

    /**
     * 设置合伙人实例id
     *
     * @param partnerInstanceId 合伙人实例id
     */
    public void setPartnerInstanceId(Long partnerInstanceId) {
        this.partnerInstanceId = partnerInstanceId;
    }
}