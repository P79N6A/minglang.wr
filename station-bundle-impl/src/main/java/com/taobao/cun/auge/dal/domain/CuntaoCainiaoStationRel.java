package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "cuntao_cainiao_station_rel")
public class CuntaoCainiaoStationRel {
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
     * 外部id
     */
    @Column(name = "object_id")
    private Long objectId;

    /**
     * 菜鸟id
     */
    @Column(name = "cainiao_station_id")
    private Long cainiaoStationId;

    /**
     * object_id类型
     */
    private String type;

    /**
     * 是否是自己的物流站
     */
    @Column(name = "is_own")
    private String isOwn;

    /**
     * 菜鸟物流站主键id
     */
    @Column(name = "logistics_station_id")
    private Long logisticsStationId;

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
     * 获取外部id
     *
     * @return object_id - 外部id
     */
    public Long getObjectId() {
        return objectId;
    }

    /**
     * 设置外部id
     *
     * @param objectId 外部id
     */
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    /**
     * 获取菜鸟id
     *
     * @return cainiao_station_id - 菜鸟id
     */
    public Long getCainiaoStationId() {
        return cainiaoStationId;
    }

    /**
     * 设置菜鸟id
     *
     * @param cainiaoStationId 菜鸟id
     */
    public void setCainiaoStationId(Long cainiaoStationId) {
        this.cainiaoStationId = cainiaoStationId;
    }

    /**
     * 获取object_id类型
     *
     * @return type - object_id类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置object_id类型
     *
     * @param type object_id类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取是否是自己的物流站
     *
     * @return is_own - 是否是自己的物流站
     */
    public String getIsOwn() {
        return isOwn;
    }

    /**
     * 设置是否是自己的物流站
     *
     * @param isOwn 是否是自己的物流站
     */
    public void setIsOwn(String isOwn) {
        this.isOwn = isOwn;
    }

    /**
     * 获取菜鸟物流站主键id
     *
     * @return logistics_station_id - 菜鸟物流站主键id
     */
    public Long getLogisticsStationId() {
        return logisticsStationId;
    }

    /**
     * 设置菜鸟物流站主键id
     *
     * @param logisticsStationId 菜鸟物流站主键id
     */
    public void setLogisticsStationId(Long logisticsStationId) {
        this.logisticsStationId = logisticsStationId;
    }
}