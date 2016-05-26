package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

public class Protocol {
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
     * 是否删除
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * 提交时间
     */
    @Column(name = "submit_time")
    private Date submitTime;

    /**
     * 协议类型
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 协议版本
     */
    private Long version;

    /**
     * 提交人
     */
    @Column(name = "submit_id")
    private String submitId;

    /**
     * 是否有效，VALID INVALID
     */
    private String state;

    /**
     * 描述
     */
    private String description;

    /**
     * 分组
     */
    @Column(name = "group_type")
    private String groupType;

    /**
     * 分组名称
     */
    @Column(name = "group_name")
    private String groupName;

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
     * 获取提交时间
     *
     * @return submit_time - 提交时间
     */
    public Date getSubmitTime() {
        return submitTime;
    }

    /**
     * 设置提交时间
     *
     * @param submitTime 提交时间
     */
    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    /**
     * 获取协议类型
     *
     * @return type - 协议类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置协议类型
     *
     * @param type 协议类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取协议版本
     *
     * @return version - 协议版本
     */
    public Long getVersion() {
        return version;
    }

    /**
     * 设置协议版本
     *
     * @param version 协议版本
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * 获取提交人
     *
     * @return submit_id - 提交人
     */
    public String getSubmitId() {
        return submitId;
    }

    /**
     * 设置提交人
     *
     * @param submitId 提交人
     */
    public void setSubmitId(String submitId) {
        this.submitId = submitId;
    }

    /**
     * 获取是否有效，VALID INVALID
     *
     * @return state - 是否有效，VALID INVALID
     */
    public String getState() {
        return state;
    }

    /**
     * 设置是否有效，VALID INVALID
     *
     * @param state 是否有效，VALID INVALID
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取分组
     *
     * @return group_type - 分组
     */
    public String getGroupType() {
        return groupType;
    }

    /**
     * 设置分组
     *
     * @param groupType 分组
     */
    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    /**
     * 获取分组名称
     *
     * @return group_name - 分组名称
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置分组名称
     *
     * @param groupName 分组名称
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}