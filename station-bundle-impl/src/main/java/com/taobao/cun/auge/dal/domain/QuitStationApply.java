package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "quit_station_apply")
public class QuitStationApply {
    /**
     * ����
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ����ʱ��
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * �޸�ʱ��
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * �Ƿ�ɾ��
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * ������
     */
    private String creator;

    /**
     * �޸���
     */
    private String modifier;

    /**
     * ����˵��
     */
    @Column(name = "other_description")
    private String otherDescription;

    /**
     * �˳�����״̬
     */
    private String state;

    /**
     * ���뵥id
     */
    @Column(name = "station_apply_id")
    private Long stationApplyId;

    /**
     * ���Գ������������
     */
    @Column(name = "revocation_app_form_file_name")
    private String revocationAppFormFileName;

    /**
     * �������֤��������
     */
    @Column(name = "loan_prove_file_name")
    private String loanProveFileName;

    /**
     * ����֤��������
     */
    @Column(name = "approval_file_name")
    private String approvalFileName;

    /**
     * �ύ������
     */
    @Column(name = "submitted_people_name")
    private String submittedPeopleName;

    /**
     * �ʲ����
     */
    @Column(name = "asset_type")
    private String assetType;

    /**
     * �����Ƿ����
     */
    @Column(name = "loan_has_close")
    private String loanHasClose;

    /**
     * �ϻ���ʵ��id
     */
    @Column(name = "partner_instance_id")
    private Long partnerInstanceId;

    /**
     * ��ȡ����
     *
     * @return id - ����
     */
    public Long getId() {
        return id;
    }

    /**
     * ��������
     *
     * @param id ����
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return gmt_create - ����ʱ��
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * ���ô���ʱ��
     *
     * @param gmtCreate ����ʱ��
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * ��ȡ�޸�ʱ��
     *
     * @return gmt_modified - �޸�ʱ��
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * �����޸�ʱ��
     *
     * @param gmtModified �޸�ʱ��
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * ��ȡ�Ƿ�ɾ��
     *
     * @return is_deleted - �Ƿ�ɾ��
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * �����Ƿ�ɾ��
     *
     * @param isDeleted �Ƿ�ɾ��
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * ��ȡ������
     *
     * @return creator - ������
     */
    public String getCreator() {
        return creator;
    }

    /**
     * ���ô�����
     *
     * @param creator ������
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * ��ȡ�޸���
     *
     * @return modifier - �޸���
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * �����޸���
     *
     * @param modifier �޸���
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * ��ȡ����˵��
     *
     * @return other_description - ����˵��
     */
    public String getOtherDescription() {
        return otherDescription;
    }

    /**
     * ��������˵��
     *
     * @param otherDescription ����˵��
     */
    public void setOtherDescription(String otherDescription) {
        this.otherDescription = otherDescription;
    }

    /**
     * ��ȡ�˳�����״̬
     *
     * @return state - �˳�����״̬
     */
    public String getState() {
        return state;
    }

    /**
     * �����˳�����״̬
     *
     * @param state �˳�����״̬
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * ��ȡ���뵥id
     *
     * @return station_apply_id - ���뵥id
     */
    public Long getStationApplyId() {
        return stationApplyId;
    }

    /**
     * �������뵥id
     *
     * @param stationApplyId ���뵥id
     */
    public void setStationApplyId(Long stationApplyId) {
        this.stationApplyId = stationApplyId;
    }

    /**
     * ��ȡ���Գ������������
     *
     * @return revocation_app_form_file_name - ���Գ������������
     */
    public String getRevocationAppFormFileName() {
        return revocationAppFormFileName;
    }

    /**
     * ���ô��Գ������������
     *
     * @param revocationAppFormFileName ���Գ������������
     */
    public void setRevocationAppFormFileName(String revocationAppFormFileName) {
        this.revocationAppFormFileName = revocationAppFormFileName;
    }

    /**
     * ��ȡ�������֤��������
     *
     * @return loan_prove_file_name - �������֤��������
     */
    public String getLoanProveFileName() {
        return loanProveFileName;
    }

    /**
     * ���ô������֤��������
     *
     * @param loanProveFileName �������֤��������
     */
    public void setLoanProveFileName(String loanProveFileName) {
        this.loanProveFileName = loanProveFileName;
    }

    /**
     * ��ȡ����֤��������
     *
     * @return approval_file_name - ����֤��������
     */
    public String getApprovalFileName() {
        return approvalFileName;
    }

    /**
     * ��������֤��������
     *
     * @param approvalFileName ����֤��������
     */
    public void setApprovalFileName(String approvalFileName) {
        this.approvalFileName = approvalFileName;
    }

    /**
     * ��ȡ�ύ������
     *
     * @return submitted_people_name - �ύ������
     */
    public String getSubmittedPeopleName() {
        return submittedPeopleName;
    }

    /**
     * �����ύ������
     *
     * @param submittedPeopleName �ύ������
     */
    public void setSubmittedPeopleName(String submittedPeopleName) {
        this.submittedPeopleName = submittedPeopleName;
    }

    /**
     * ��ȡ�ʲ����
     *
     * @return asset_type - �ʲ����
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * �����ʲ����
     *
     * @param assetType �ʲ����
     */
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    /**
     * ��ȡ�����Ƿ����
     *
     * @return loan_has_close - �����Ƿ����
     */
    public String getLoanHasClose() {
        return loanHasClose;
    }

    /**
     * ���ô����Ƿ����
     *
     * @param loanHasClose �����Ƿ����
     */
    public void setLoanHasClose(String loanHasClose) {
        this.loanHasClose = loanHasClose;
    }

    /**
     * ��ȡ�ϻ���ʵ��id
     *
     * @return partner_instance_id - �ϻ���ʵ��id
     */
    public Long getPartnerInstanceId() {
        return partnerInstanceId;
    }

    /**
     * ���úϻ���ʵ��id
     *
     * @param partnerInstanceId �ϻ���ʵ��id
     */
    public void setPartnerInstanceId(Long partnerInstanceId) {
        this.partnerInstanceId = partnerInstanceId;
    }
}