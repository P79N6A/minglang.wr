package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

public class Partner {
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
     * 姓名
     */
    private String name;

    /**
     * 支付宝账号
     */
    @Column(name = "alipay_account")
    private String alipayAccount;

    /**
     * 淘宝user_id
     */
    @Column(name = "taobao_user_id")
    private Long taobaoUserId;

    /**
     * 淘宝nick
     */
    @Column(name = "taobao_nick")
    private String taobaoNick;

    /**
     * 身份证号码
     */
    @Column(name = "iden_num")
    private String idenNum;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * email
     */
    private String email;

    /**
     * 经营类型全职： fulltime 兼职：partime
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * 现状描述
     */
    private String description;

    /**
     * 状态：temp:暂存，normal:正常
     */
    private String state;

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
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取支付宝账号
     *
     * @return alipay_account - 支付宝账号
     */
    public String getAlipayAccount() {
        return alipayAccount;
    }

    /**
     * 设置支付宝账号
     *
     * @param alipayAccount 支付宝账号
     */
    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    /**
     * 获取淘宝user_id
     *
     * @return taobao_user_id - 淘宝user_id
     */
    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    /**
     * 设置淘宝user_id
     *
     * @param taobaoUserId 淘宝user_id
     */
    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    /**
     * 获取淘宝nick
     *
     * @return taobao_nick - 淘宝nick
     */
    public String getTaobaoNick() {
        return taobaoNick;
    }

    /**
     * 设置淘宝nick
     *
     * @param taobaoNick 淘宝nick
     */
    public void setTaobaoNick(String taobaoNick) {
        this.taobaoNick = taobaoNick;
    }

    /**
     * 获取身份证号码
     *
     * @return iden_num - 身份证号码
     */
    public String getIdenNum() {
        return idenNum;
    }

    /**
     * 设置身份证号码
     *
     * @param idenNum 身份证号码
     */
    public void setIdenNum(String idenNum) {
        this.idenNum = idenNum;
    }

    /**
     * 获取手机号码
     *
     * @return mobile - 手机号码
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号码
     *
     * @param mobile 手机号码
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取email
     *
     * @return email - email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置email
     *
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取经营类型全职： fulltime 兼职：partime
     *
     * @return business_type - 经营类型全职： fulltime 兼职：partime
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * 设置经营类型全职： fulltime 兼职：partime
     *
     * @param businessType 经营类型全职： fulltime 兼职：partime
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * 获取现状描述
     *
     * @return description - 现状描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置现状描述
     *
     * @param description 现状描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取状态：temp:暂存，normal:正常
     *
     * @return state - 状态：temp:暂存，normal:正常
     */
    public String getState() {
        return state;
    }

    /**
     * 设置状态：temp:暂存，normal:正常
     *
     * @param state 状态：temp:暂存，normal:正常
     */
    public void setState(String state) {
        this.state = state;
    }
}