package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

public class Station {
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
    private String creater;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 服务站名称
     */
    private String name;

    /**
     * 服务站简介
     */
    private String description;

    /**
     * 支付宝账号
     */
    @Column(name = "alipay_account")
    private String alipayAccount;

    /**
     * 集团会员nick
     */
    @Column(name = "taobao_nick")
    private String taobaoNick;

    /**
     * 服务点状态
     */
    private String state;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * 省编码
     */
    private String province;

    /**
     * 市编码
     */
    private String city;

    /**
     * 县/区编码
     */
    private String county;

    /**
     * 乡镇编码
     */
    private String town;

    /**
     * 省详细
     */
    @Column(name = "province_detail")
    private String provinceDetail;

    /**
     * 市详细
     */
    @Column(name = "city_detail")
    private String cityDetail;

    /**
     * 县/区详细
     */
    @Column(name = "county_detail")
    private String countyDetail;

    /**
     * 乡镇详细
     */
    @Column(name = "town_detail")
    private String townDetail;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 申请组织
     */
    @Column(name = "apply_org")
    private Long applyOrg;

    /**
     * 淘宝用户id
     */
    @Column(name = "taobao_user_id")
    private Long taobaoUserId;

    /**
     * 村点编号
     */
    @Column(name = "station_num")
    private String stationNum;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 村/社区编号
     */
    private String village;

    /**
     * 村/社区名称
详情
     */
    @Column(name = "village_detail")
    private String villageDetail;

    /**
     * 覆盖人口
     */
    private String covered;

    /**
     * 特色农副产品
     */
    private String products;

    /**
     * 物流状态
     */
    @Column(name = "logistics_state")
    private String logisticsState;

    /**
     * 目前业态
     */
    private String format;

    /**
     * 固点，或者不固点
     */
    @Column(name = "area_type")
    private String areaType;

    /**
     * 管理员user_id
     */
    @Column(name = "manager_id")
    private String managerId;

    /**
     * 服务商id
     */
    @Column(name = "provider_id")
    private Long providerId;

    /**
     * 其他特性，用于扩展服务站属性
     */
    private String feature;

    /**
     * 新的服务站状态
     */
    private String status;

    /**
     * 场地固点类型 GOV_FIXED 政府固点
TRIPARTITE_FIXED
三方固点
     */
    @Column(name = "fixed_type")
    private String fixedType;

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
     * @return creater - 创建人
     */
    public String getCreater() {
        return creater;
    }

    /**
     * 设置创建人
     *
     * @param creater 创建人
     */
    public void setCreater(String creater) {
        this.creater = creater;
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
     * 获取服务站名称
     *
     * @return name - 服务站名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置服务站名称
     *
     * @param name 服务站名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取服务站简介
     *
     * @return description - 服务站简介
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置服务站简介
     *
     * @param description 服务站简介
     */
    public void setDescription(String description) {
        this.description = description;
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
     * 获取集团会员nick
     *
     * @return taobao_nick - 集团会员nick
     */
    public String getTaobaoNick() {
        return taobaoNick;
    }

    /**
     * 设置集团会员nick
     *
     * @param taobaoNick 集团会员nick
     */
    public void setTaobaoNick(String taobaoNick) {
        this.taobaoNick = taobaoNick;
    }

    /**
     * 获取服务点状态
     *
     * @return state - 服务点状态
     */
    public String getState() {
        return state;
    }

    /**
     * 设置服务点状态
     *
     * @param state 服务点状态
     */
    public void setState(String state) {
        this.state = state;
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
     * 获取省编码
     *
     * @return province - 省编码
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省编码
     *
     * @param province 省编码
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取市编码
     *
     * @return city - 市编码
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市编码
     *
     * @param city 市编码
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取县/区编码
     *
     * @return county - 县/区编码
     */
    public String getCounty() {
        return county;
    }

    /**
     * 设置县/区编码
     *
     * @param county 县/区编码
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * 获取乡镇编码
     *
     * @return town - 乡镇编码
     */
    public String getTown() {
        return town;
    }

    /**
     * 设置乡镇编码
     *
     * @param town 乡镇编码
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * 获取省详细
     *
     * @return province_detail - 省详细
     */
    public String getProvinceDetail() {
        return provinceDetail;
    }

    /**
     * 设置省详细
     *
     * @param provinceDetail 省详细
     */
    public void setProvinceDetail(String provinceDetail) {
        this.provinceDetail = provinceDetail;
    }

    /**
     * 获取市详细
     *
     * @return city_detail - 市详细
     */
    public String getCityDetail() {
        return cityDetail;
    }

    /**
     * 设置市详细
     *
     * @param cityDetail 市详细
     */
    public void setCityDetail(String cityDetail) {
        this.cityDetail = cityDetail;
    }

    /**
     * 获取县/区详细
     *
     * @return county_detail - 县/区详细
     */
    public String getCountyDetail() {
        return countyDetail;
    }

    /**
     * 设置县/区详细
     *
     * @param countyDetail 县/区详细
     */
    public void setCountyDetail(String countyDetail) {
        this.countyDetail = countyDetail;
    }

    /**
     * 获取乡镇详细
     *
     * @return town_detail - 乡镇详细
     */
    public String getTownDetail() {
        return townDetail;
    }

    /**
     * 设置乡镇详细
     *
     * @param townDetail 乡镇详细
     */
    public void setTownDetail(String townDetail) {
        this.townDetail = townDetail;
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
     * 获取申请组织
     *
     * @return apply_org - 申请组织
     */
    public Long getApplyOrg() {
        return applyOrg;
    }

    /**
     * 设置申请组织
     *
     * @param applyOrg 申请组织
     */
    public void setApplyOrg(Long applyOrg) {
        this.applyOrg = applyOrg;
    }

    /**
     * 获取淘宝用户id
     *
     * @return taobao_user_id - 淘宝用户id
     */
    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    /**
     * 设置淘宝用户id
     *
     * @param taobaoUserId 淘宝用户id
     */
    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    /**
     * 获取村点编号
     *
     * @return station_num - 村点编号
     */
    public String getStationNum() {
        return stationNum;
    }

    /**
     * 设置村点编号
     *
     * @param stationNum 村点编号
     */
    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
    }

    /**
     * 获取经度
     *
     * @return lng - 经度
     */
    public String getLng() {
        return lng;
    }

    /**
     * 设置经度
     *
     * @param lng 经度
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     * 获取纬度
     *
     * @return lat - 纬度
     */
    public String getLat() {
        return lat;
    }

    /**
     * 设置纬度
     *
     * @param lat 纬度
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * 获取村/社区编号
     *
     * @return village - 村/社区编号
     */
    public String getVillage() {
        return village;
    }

    /**
     * 设置村/社区编号
     *
     * @param village 村/社区编号
     */
    public void setVillage(String village) {
        this.village = village;
    }

    /**
     * 获取村/社区名称
详情
     *
     * @return village_detail - 村/社区名称
详情
     */
    public String getVillageDetail() {
        return villageDetail;
    }

    /**
     * 设置村/社区名称
详情
     *
     * @param villageDetail 村/社区名称
详情
     */
    public void setVillageDetail(String villageDetail) {
        this.villageDetail = villageDetail;
    }

    /**
     * 获取覆盖人口
     *
     * @return covered - 覆盖人口
     */
    public String getCovered() {
        return covered;
    }

    /**
     * 设置覆盖人口
     *
     * @param covered 覆盖人口
     */
    public void setCovered(String covered) {
        this.covered = covered;
    }

    /**
     * 获取特色农副产品
     *
     * @return products - 特色农副产品
     */
    public String getProducts() {
        return products;
    }

    /**
     * 设置特色农副产品
     *
     * @param products 特色农副产品
     */
    public void setProducts(String products) {
        this.products = products;
    }

    /**
     * 获取物流状态
     *
     * @return logistics_state - 物流状态
     */
    public String getLogisticsState() {
        return logisticsState;
    }

    /**
     * 设置物流状态
     *
     * @param logisticsState 物流状态
     */
    public void setLogisticsState(String logisticsState) {
        this.logisticsState = logisticsState;
    }

    /**
     * 获取目前业态
     *
     * @return format - 目前业态
     */
    public String getFormat() {
        return format;
    }

    /**
     * 设置目前业态
     *
     * @param format 目前业态
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 获取固点，或者不固点
     *
     * @return area_type - 固点，或者不固点
     */
    public String getAreaType() {
        return areaType;
    }

    /**
     * 设置固点，或者不固点
     *
     * @param areaType 固点，或者不固点
     */
    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    /**
     * 获取管理员user_id
     *
     * @return manager_id - 管理员user_id
     */
    public String getManagerId() {
        return managerId;
    }

    /**
     * 设置管理员user_id
     *
     * @param managerId 管理员user_id
     */
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    /**
     * 获取服务商id
     *
     * @return provider_id - 服务商id
     */
    public Long getProviderId() {
        return providerId;
    }

    /**
     * 设置服务商id
     *
     * @param providerId 服务商id
     */
    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    /**
     * 获取其他特性，用于扩展服务站属性
     *
     * @return feature - 其他特性，用于扩展服务站属性
     */
    public String getFeature() {
        return feature;
    }

    /**
     * 设置其他特性，用于扩展服务站属性
     *
     * @param feature 其他特性，用于扩展服务站属性
     */
    public void setFeature(String feature) {
        this.feature = feature;
    }

    /**
     * 获取新的服务站状态
     *
     * @return status - 新的服务站状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置新的服务站状态
     *
     * @param status 新的服务站状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取场地固点类型 GOV_FIXED 政府固点
TRIPARTITE_FIXED
三方固点
     *
     * @return fixed_type - 场地固点类型 GOV_FIXED 政府固点
TRIPARTITE_FIXED
三方固点
     */
    public String getFixedType() {
        return fixedType;
    }

    /**
     * 设置场地固点类型 GOV_FIXED 政府固点
TRIPARTITE_FIXED
三方固点
     *
     * @param fixedType 场地固点类型 GOV_FIXED 政府固点
TRIPARTITE_FIXED
三方固点
     */
    public void setFixedType(String fixedType) {
        this.fixedType = fixedType;
    }
}