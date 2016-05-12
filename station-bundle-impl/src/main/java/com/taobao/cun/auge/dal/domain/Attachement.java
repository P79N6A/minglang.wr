package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

public class Attachement {
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
     * 外部的存储id
     */
    @Column(name = "fs_id")
    private String fsId;

    /**
     * 附件标题
     */
    private String title;

    /**
     * 文件类型,比如txt,jpg
     */
    @Column(name = "file_type")
    private String fileType;

    /**
     * 附件描述
     */
    private String description;

    /**
     * 附件类型id
     */
    @Column(name = "attachement_type_id")
    private Long attachementTypeId;

    /**
     * 外部id,如station_apply表id
     */
    @Column(name = "object_id")
    private Long objectId;

    /**
     * 业务类型
     */
    @Column(name = "biz_type")
    private String bizType;

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
     * 获取外部的存储id
     *
     * @return fs_id - 外部的存储id
     */
    public String getFsId() {
        return fsId;
    }

    /**
     * 设置外部的存储id
     *
     * @param fsId 外部的存储id
     */
    public void setFsId(String fsId) {
        this.fsId = fsId;
    }

    /**
     * 获取附件标题
     *
     * @return title - 附件标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置附件标题
     *
     * @param title 附件标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取文件类型,比如txt,jpg
     *
     * @return file_type - 文件类型,比如txt,jpg
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * 设置文件类型,比如txt,jpg
     *
     * @param fileType 文件类型,比如txt,jpg
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * 获取附件描述
     *
     * @return description - 附件描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置附件描述
     *
     * @param description 附件描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取附件类型id
     *
     * @return attachement_type_id - 附件类型id
     */
    public Long getAttachementTypeId() {
        return attachementTypeId;
    }

    /**
     * 设置附件类型id
     *
     * @param attachementTypeId 附件类型id
     */
    public void setAttachementTypeId(Long attachementTypeId) {
        this.attachementTypeId = attachementTypeId;
    }

    /**
     * 获取外部id,如station_apply表id
     *
     * @return object_id - 外部id,如station_apply表id
     */
    public Long getObjectId() {
        return objectId;
    }

    /**
     * 设置外部id,如station_apply表id
     *
     * @param objectId 外部id,如station_apply表id
     */
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    /**
     * 获取业务类型
     *
     * @return biz_type - 业务类型
     */
    public String getBizType() {
        return bizType;
    }

    /**
     * 设置业务类型
     *
     * @param bizType 业务类型
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
}