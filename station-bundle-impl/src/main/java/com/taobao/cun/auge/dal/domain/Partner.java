package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

public class Partner {
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
     * ����
     */
    private String name;

    /**
     * ֧�����˺�
     */
    @Column(name = "alipay_account")
    private String alipayAccount;

    /**
     * �Ա�user_id
     */
    @Column(name = "taobao_user_id")
    private Long taobaoUserId;

    /**
     * �Ա�nick
     */
    @Column(name = "taobao_nick")
    private String taobaoNick;

    /**
     * ���֤����
     */
    @Column(name = "iden_num")
    private String idenNum;

    /**
     * �ֻ�����
     */
    private String mobile;

    /**
     * email
     */
    private String email;

    /**
     * ��Ӫ����ȫְ�� fulltime ��ְ��partime
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * ��״����
     */
    private String description;

    /**
     * ״̬��temp:�ݴ棬normal:����
     */
    private String state;

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
     * ��ȡ����
     *
     * @return name - ����
     */
    public String getName() {
        return name;
    }

    /**
     * ��������
     *
     * @param name ����
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * ��ȡ�Ա�user_id
     *
     * @return taobao_user_id - �Ա�user_id
     */
    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    /**
     * �����Ա�user_id
     *
     * @param taobaoUserId �Ա�user_id
     */
    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    /**
     * ��ȡ�Ա�nick
     *
     * @return taobao_nick - �Ա�nick
     */
    public String getTaobaoNick() {
        return taobaoNick;
    }

    /**
     * �����Ա�nick
     *
     * @param taobaoNick �Ա�nick
     */
    public void setTaobaoNick(String taobaoNick) {
        this.taobaoNick = taobaoNick;
    }

    /**
     * ��ȡ���֤����
     *
     * @return iden_num - ���֤����
     */
    public String getIdenNum() {
        return idenNum;
    }

    /**
     * �������֤����
     *
     * @param idenNum ���֤����
     */
    public void setIdenNum(String idenNum) {
        this.idenNum = idenNum;
    }

    /**
     * ��ȡ�ֻ�����
     *
     * @return mobile - �ֻ�����
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * �����ֻ�����
     *
     * @param mobile �ֻ�����
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * ��ȡemail
     *
     * @return email - email
     */
    public String getEmail() {
        return email;
    }

    /**
     * ����email
     *
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * ��ȡ��Ӫ����ȫְ�� fulltime ��ְ��partime
     *
     * @return business_type - ��Ӫ����ȫְ�� fulltime ��ְ��partime
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * ���þ�Ӫ����ȫְ�� fulltime ��ְ��partime
     *
     * @param businessType ��Ӫ����ȫְ�� fulltime ��ְ��partime
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * ��ȡ��״����
     *
     * @return description - ��״����
     */
    public String getDescription() {
        return description;
    }

    /**
     * ������״����
     *
     * @param description ��״����
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * ��ȡ״̬��temp:�ݴ棬normal:����
     *
     * @return state - ״̬��temp:�ݴ棬normal:����
     */
    public String getState() {
        return state;
    }

    /**
     * ����״̬��temp:�ݴ棬normal:����
     *
     * @param state ״̬��temp:�ݴ棬normal:����
     */
    public void setState(String state) {
        this.state = state;
    }
}