package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "logistics_station_apply")
public class LogisticsStationApply {
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
     * 逻辑删除标志位
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * 申请人stationId
     */
    @Column(name = "applier_station_id")
    private Long applierStationId;

    /**
     * 省编码
     */
    @Column(name = "province_id")
    private Long provinceId;

    /**
     * 省名称
     */
    private String province;

    /**
     * 市编码
     */
    @Column(name = "city_id")
    private Long cityId;

    /**
     * 市名称
     */
    private String city;

    /**
     * 县编码
     */
    @Column(name = "county_id")
    private Long countyId;

    /**
     * 县名称
     */
    private String county;

    /**
     * 镇编码
     */
    @Column(name = "town_id")
    private Long townId;

    /**
     * 镇名称
     */
    private String town;

    /**
     * 村编码
     */
    @Column(name = "country_id")
    private Long countryId;

    /**
     * 村名称
     */
    private String country;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 申请人名
     */
    @Column(name = "applier_name")
    private String applierName;

    /**
     * 申请人手机号
     */
    private String mobile;

    /**
     * 合伙人的淘宝USERID
     */
    @Column(name = "taobao_user_id")
    private Long taobaoUserId;

    /**
     * 合伙人的站点ID
     */
    @Column(name = "station_id")
    private Long stationId;

    /**
     * 申请人的淘宝UserId
     */
    @Column(name = "applier_taobao_user_id")
    private Long applierTaobaoUserId;

    /**
     * 菜鸟打标类型
     */
    @Column(name = "ctp_type")
    private String ctpType;

    /**
     * 申请人的组织ID
     */
    @Column(name = "applier_org_id")
    private Long applierOrgId;

    /**
     * 物流站点ID
     */
    @Column(name = "logistics_station_id")
    private Long logisticsStationId;

    /**
     * 申请状态
     */
    @Column(name = "apply_status")
    private String applyStatus;

    /**
     * 审核备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String modifer;

    /**
     * 类型
     */
    private String type;

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
     * 获取逻辑删除标志位
     *
     * @return is_deleted - 逻辑删除标志位
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置逻辑删除标志位
     *
     * @param isDeleted 逻辑删除标志位
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取申请人stationId
     *
     * @return applier_station_id - 申请人stationId
     */
    public Long getApplierStationId() {
        return applierStationId;
    }

    /**
     * 设置申请人stationId
     *
     * @param applierStationId 申请人stationId
     */
    public void setApplierStationId(Long applierStationId) {
        this.applierStationId = applierStationId;
    }

    /**
     * 获取省编码
     *
     * @return province_id - 省编码
     */
    public Long getProvinceId() {
        return provinceId;
    }

    /**
     * 设置省编码
     *
     * @param provinceId 省编码
     */
    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * 获取省名称
     *
     * @return province - 省名称
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省名称
     *
     * @param province 省名称
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取市编码
     *
     * @return city_id - 市编码
     */
    public Long getCityId() {
        return cityId;
    }

    /**
     * 设置市编码
     *
     * @param cityId 市编码
     */
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    /**
     * 获取市名称
     *
     * @return city - 市名称
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市名称
     *
     * @param city 市名称
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取县编码
     *
     * @return county_id - 县编码
     */
    public Long getCountyId() {
        return countyId;
    }

    /**
     * 设置县编码
     *
     * @param countyId 县编码
     */
    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    /**
     * 获取县名称
     *
     * @return county - 县名称
     */
    public String getCounty() {
        return county;
    }

    /**
     * 设置县名称
     *
     * @param county 县名称
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * 获取镇编码
     *
     * @return town_id - 镇编码
     */
    public Long getTownId() {
        return townId;
    }

    /**
     * 设置镇编码
     *
     * @param townId 镇编码
     */
    public void setTownId(Long townId) {
        this.townId = townId;
    }

    /**
     * 获取镇名称
     *
     * @return town - 镇名称
     */
    public String getTown() {
        return town;
    }

    /**
     * 设置镇名称
     *
     * @param town 镇名称
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * 获取村编码
     *
     * @return country_id - 村编码
     */
    public Long getCountryId() {
        return countryId;
    }

    /**
     * 设置村编码
     *
     * @param countryId 村编码
     */
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    /**
     * 获取村名称
     *
     * @return country - 村名称
     */
    public String getCountry() {
        return country;
    }

    /**
     * 设置村名称
     *
     * @param country 村名称
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 获取详细地址
     *
     * @return address - 详细地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置详细地址
     *
     * @param address 详细地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取申请人名
     *
     * @return applier_name - 申请人名
     */
    public String getApplierName() {
        return applierName;
    }

    /**
     * 设置申请人名
     *
     * @param applierName 申请人名
     */
    public void setApplierName(String applierName) {
        this.applierName = applierName;
    }

    /**
     * 获取申请人手机号
     *
     * @return mobile - 申请人手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置申请人手机号
     *
     * @param mobile 申请人手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取合伙人的淘宝USERID
     *
     * @return taobao_user_id - 合伙人的淘宝USERID
     */
    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    /**
     * 设置合伙人的淘宝USERID
     *
     * @param taobaoUserId 合伙人的淘宝USERID
     */
    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    /**
     * 获取合伙人的站点ID
     *
     * @return station_id - 合伙人的站点ID
     */
    public Long getStationId() {
        return stationId;
    }

    /**
     * 设置合伙人的站点ID
     *
     * @param stationId 合伙人的站点ID
     */
    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    /**
     * 获取申请人的淘宝UserId
     *
     * @return applier_taobao_user_id - 申请人的淘宝UserId
     */
    public Long getApplierTaobaoUserId() {
        return applierTaobaoUserId;
    }

    /**
     * 设置申请人的淘宝UserId
     *
     * @param applierTaobaoUserId 申请人的淘宝UserId
     */
    public void setApplierTaobaoUserId(Long applierTaobaoUserId) {
        this.applierTaobaoUserId = applierTaobaoUserId;
    }

    /**
     * 获取菜鸟打标类型
     *
     * @return ctp_type - 菜鸟打标类型
     */
    public String getCtpType() {
        return ctpType;
    }

    /**
     * 设置菜鸟打标类型
     *
     * @param ctpType 菜鸟打标类型
     */
    public void setCtpType(String ctpType) {
        this.ctpType = ctpType;
    }

    /**
     * 获取申请人的组织ID
     *
     * @return applier_org_id - 申请人的组织ID
     */
    public Long getApplierOrgId() {
        return applierOrgId;
    }

    /**
     * 设置申请人的组织ID
     *
     * @param applierOrgId 申请人的组织ID
     */
    public void setApplierOrgId(Long applierOrgId) {
        this.applierOrgId = applierOrgId;
    }

    /**
     * 获取物流站点ID
     *
     * @return logistics_station_id - 物流站点ID
     */
    public Long getLogisticsStationId() {
        return logisticsStationId;
    }

    /**
     * 设置物流站点ID
     *
     * @param logisticsStationId 物流站点ID
     */
    public void setLogisticsStationId(Long logisticsStationId) {
        this.logisticsStationId = logisticsStationId;
    }

    /**
     * 获取申请状态
     *
     * @return apply_status - 申请状态
     */
    public String getApplyStatus() {
        return applyStatus;
    }

    /**
     * 设置申请状态
     *
     * @param applyStatus 申请状态
     */
    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    /**
     * 获取审核备注
     *
     * @return remark - 审核备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置审核备注
     *
     * @param remark 审核备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * @return modifer - 修改人
     */
    public String getModifer() {
        return modifer;
    }

    /**
     * 设置修改人
     *
     * @param modifer 修改人
     */
    public void setModifer(String modifer) {
        this.modifer = modifer;
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
}