package com.taobao.cun.auge.dal.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "account_money")
public class AccountMoney {
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
     * 账户用户id
     */
    @Column(name = "taobao_user_id")
    private String taobaoUserId;

    /**
     * 冻结时间
     */
    @Column(name = "frozen_time")
    private Date frozenTime;

    /**
     * 解冻时间
     */
    @Column(name = "thaw_time")
    private Date thawTime;

    /**
     * 钱
     */
    private BigDecimal money;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private String state;

    /**
     * 业务类型
     */
    @Column(name = "target_type")
    private String targetType;

    /**
     * 业务id
     */
    @Column(name = "object_id")
    private Long objectId;

    /**
     * 支付宝账号
     */
    @Column(name = "alipay_account")
    private String alipayAccount;

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
     * 获取账户用户id
     *
     * @return taobao_user_id - 账户用户id
     */
    public String getTaobaoUserId() {
        return taobaoUserId;
    }

    /**
     * 设置账户用户id
     *
     * @param taobaoUserId 账户用户id
     */
    public void setTaobaoUserId(String taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    /**
     * 获取冻结时间
     *
     * @return frozen_time - 冻结时间
     */
    public Date getFrozenTime() {
        return frozenTime;
    }

    /**
     * 设置冻结时间
     *
     * @param frozenTime 冻结时间
     */
    public void setFrozenTime(Date frozenTime) {
        this.frozenTime = frozenTime;
    }

    /**
     * 获取解冻时间
     *
     * @return thaw_time - 解冻时间
     */
    public Date getThawTime() {
        return thawTime;
    }

    /**
     * 设置解冻时间
     *
     * @param thawTime 解冻时间
     */
    public void setThawTime(Date thawTime) {
        this.thawTime = thawTime;
    }

    /**
     * 获取钱
     *
     * @return money - 钱
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置钱
     *
     * @param money 钱
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(String type) {
        this.type = type;
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
     * 获取业务id
     *
     * @return object_id - 业务id
     */
    public Long getObjectId() {
        return objectId;
    }

    /**
     * 设置业务id
     *
     * @param objectId 业务id
     */
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
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
}