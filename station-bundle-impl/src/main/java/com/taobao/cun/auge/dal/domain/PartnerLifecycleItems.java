package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "partner_lifecycle_items")
public class PartnerLifecycleItems {
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
     * ������
     */
    private String creator;

    /**
     * �޸���
     */
    private String modifier;

    /**
     * �ϻ�������
     */
    @Column(name = "partner_type")
    private String partnerType;

    /**
     * ҵ������
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * ��פЭ��ǩ���ǩ
     */
    @Column(name = "settled_protocol")
    private String settledProtocol;

    /**
     * ��֤���ǩ
     */
    private String bond;

    /**
     * �˳�Э���ǩ
     */
    @Column(name = "quit_protocol")
    private String quitProtocol;

    /**
     * ��������
     */
    @Column(name = "logistics_approve")
    private String logisticsApprove;

    /**
     * �ϻ���ʵ��������
     */
    @Column(name = "partner_instance_id")
    private Long partnerInstanceId;

    /**
     * �Ƿ��Ѿ�ɾ��
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * ͬһҵ�������У���ǰ��������
     */
    @Column(name = "current_step")
    private String currentStep;

    /**
     * ��ɫ����
     */
    @Column(name = "role_approve")
    private String roleApprove;

    /**
     * С��ȷ��
     */
    private String confirm;

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
     * ��ȡ�ϻ�������
     *
     * @return partner_type - �ϻ�������
     */
    public String getPartnerType() {
        return partnerType;
    }

    /**
     * ���úϻ�������
     *
     * @param partnerType �ϻ�������
     */
    public void setPartnerType(String partnerType) {
        this.partnerType = partnerType;
    }

    /**
     * ��ȡҵ������
     *
     * @return business_type - ҵ������
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * ����ҵ������
     *
     * @param businessType ҵ������
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * ��ȡ��פЭ��ǩ���ǩ
     *
     * @return settled_protocol - ��פЭ��ǩ���ǩ
     */
    public String getSettledProtocol() {
        return settledProtocol;
    }

    /**
     * ������פЭ��ǩ���ǩ
     *
     * @param settledProtocol ��פЭ��ǩ���ǩ
     */
    public void setSettledProtocol(String settledProtocol) {
        this.settledProtocol = settledProtocol;
    }

    /**
     * ��ȡ��֤���ǩ
     *
     * @return bond - ��֤���ǩ
     */
    public String getBond() {
        return bond;
    }

    /**
     * ���ñ�֤���ǩ
     *
     * @param bond ��֤���ǩ
     */
    public void setBond(String bond) {
        this.bond = bond;
    }

    /**
     * ��ȡ�˳�Э���ǩ
     *
     * @return quit_protocol - �˳�Э���ǩ
     */
    public String getQuitProtocol() {
        return quitProtocol;
    }

    /**
     * �����˳�Э���ǩ
     *
     * @param quitProtocol �˳�Э���ǩ
     */
    public void setQuitProtocol(String quitProtocol) {
        this.quitProtocol = quitProtocol;
    }

    /**
     * ��ȡ��������
     *
     * @return logistics_approve - ��������
     */
    public String getLogisticsApprove() {
        return logisticsApprove;
    }

    /**
     * ������������
     *
     * @param logisticsApprove ��������
     */
    public void setLogisticsApprove(String logisticsApprove) {
        this.logisticsApprove = logisticsApprove;
    }

    /**
     * ��ȡ�ϻ���ʵ��������
     *
     * @return partner_instance_id - �ϻ���ʵ��������
     */
    public Long getPartnerInstanceId() {
        return partnerInstanceId;
    }

    /**
     * ���úϻ���ʵ��������
     *
     * @param partnerInstanceId �ϻ���ʵ��������
     */
    public void setPartnerInstanceId(Long partnerInstanceId) {
        this.partnerInstanceId = partnerInstanceId;
    }

    /**
     * ��ȡ�Ƿ��Ѿ�ɾ��
     *
     * @return is_deleted - �Ƿ��Ѿ�ɾ��
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * �����Ƿ��Ѿ�ɾ��
     *
     * @param isDeleted �Ƿ��Ѿ�ɾ��
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * ��ȡͬһҵ�������У���ǰ��������
     *
     * @return current_step - ͬһҵ�������У���ǰ��������
     */
    public String getCurrentStep() {
        return currentStep;
    }

    /**
     * ����ͬһҵ�������У���ǰ��������
     *
     * @param currentStep ͬһҵ�������У���ǰ��������
     */
    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    /**
     * ��ȡ��ɫ����
     *
     * @return role_approve - ��ɫ����
     */
    public String getRoleApprove() {
        return roleApprove;
    }

    /**
     * ���ý�ɫ����
     *
     * @param roleApprove ��ɫ����
     */
    public void setRoleApprove(String roleApprove) {
        this.roleApprove = roleApprove;
    }

    /**
     * ��ȡС��ȷ��
     *
     * @return confirm - С��ȷ��
     */
    public String getConfirm() {
        return confirm;
    }

    /**
     * ����С��ȷ��
     *
     * @param confirm С��ȷ��
     */
    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}