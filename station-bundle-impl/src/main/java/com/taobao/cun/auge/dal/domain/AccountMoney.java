package com.taobao.cun.auge.dal.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "account_money")
public class AccountMoney {
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
     * �Ƿ���ɾ��
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * �˻��û�id
     */
    @Column(name = "taobao_user_id")
    private String taobaoUserId;

    /**
     * ����ʱ��
     */
    @Column(name = "frozen_time")
    private Date frozenTime;

    /**
     * �ⶳʱ��
     */
    @Column(name = "thaw_time")
    private Date thawTime;

    /**
     * Ǯ
     */
    private BigDecimal money;

    /**
     * ����
     */
    private String type;

    /**
     * ״̬
     */
    private String state;

    /**
     * ҵ������
     */
    @Column(name = "target_type")
    private String targetType;

    /**
     * ҵ��id
     */
    @Column(name = "object_id")
    private Long objectId;

    /**
     * ֧�����˺�
     */
    @Column(name = "alipay_account")
    private String alipayAccount;

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
     * ��ȡ�Ƿ���ɾ��
     *
     * @return is_deleted - �Ƿ���ɾ��
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * �����Ƿ���ɾ��
     *
     * @param isDeleted �Ƿ���ɾ��
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * ��ȡ�˻��û�id
     *
     * @return taobao_user_id - �˻��û�id
     */
    public String getTaobaoUserId() {
        return taobaoUserId;
    }

    /**
     * �����˻��û�id
     *
     * @param taobaoUserId �˻��û�id
     */
    public void setTaobaoUserId(String taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return frozen_time - ����ʱ��
     */
    public Date getFrozenTime() {
        return frozenTime;
    }

    /**
     * ���ö���ʱ��
     *
     * @param frozenTime ����ʱ��
     */
    public void setFrozenTime(Date frozenTime) {
        this.frozenTime = frozenTime;
    }

    /**
     * ��ȡ�ⶳʱ��
     *
     * @return thaw_time - �ⶳʱ��
     */
    public Date getThawTime() {
        return thawTime;
    }

    /**
     * ���ýⶳʱ��
     *
     * @param thawTime �ⶳʱ��
     */
    public void setThawTime(Date thawTime) {
        this.thawTime = thawTime;
    }

    /**
     * ��ȡǮ
     *
     * @return money - Ǯ
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * ����Ǯ
     *
     * @param money Ǯ
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * ��ȡ����
     *
     * @return type - ����
     */
    public String getType() {
        return type;
    }

    /**
     * ��������
     *
     * @param type ����
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * ��ȡ״̬
     *
     * @return state - ״̬
     */
    public String getState() {
        return state;
    }

    /**
     * ����״̬
     *
     * @param state ״̬
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * ��ȡҵ������
     *
     * @return target_type - ҵ������
     */
    public String getTargetType() {
        return targetType;
    }

    /**
     * ����ҵ������
     *
     * @param targetType ҵ������
     */
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    /**
     * ��ȡҵ��id
     *
     * @return object_id - ҵ��id
     */
    public Long getObjectId() {
        return objectId;
    }

    /**
     * ����ҵ��id
     *
     * @param objectId ҵ��id
     */
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    /**
     * ��ȡ֧�����˺�
     *
     * @return alipay_account - ֧�����˺�
     */
    public String getAlipayAccount() {
        return alipayAccount;
    }

    /**
     * ����֧�����˺�
     *
     * @param alipayAccount ֧�����˺�
     */
    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }
}