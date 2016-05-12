package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "logistics_station")
public class LogisticsStation {
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
     * 省
     */
    private String province;

    /**
     * 省名称
     */
    @Column(name = "province_detail")
    private String provinceDetail;

    /**
     * 市
     */
    private String city;

    /**
     * 市名称
     */
    @Column(name = "city_detail")
    private String cityDetail;

    /**
     * 县
     */
    private String county;

    /**
     * 县名称
     */
    @Column(name = "county_detail")
    private String countyDetail;

    /**
     * 镇
     */
    private String town;

    /**
     * 镇名称
     */
    @Column(name = "town_detail")
    private String townDetail;

    /**
     * 详细地址
     */
    @Column(name = "address_detail")
    private String addressDetail;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 村
     */
    private String village;

    /**
     * 村名称
     */
    @Column(name = "village_detail")
    private String villageDetail;

    /**
     * 菜鸟物流站id
     */
    @Column(name = "cainiao_station_id")
    private Long cainiaoStationId;

    /**
     * 物流服务站名称
     */
    private String name;

    /**
     * 联系人姓名
     */
    @Column(name = "contact_name")
    private String contactName;

    /**
     * 上级物流站id
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 物流站类型，3=县物流站点、4=村物流站点
     */
    @Column(name = "station_type")
    private Integer stationType;

    /**
     * 状态,村淘都是线下=-1
     */
    private Integer status;

    /**
     * 联系人手机号码
     */
    @Column(name = "contact_mobile")
    private String contactMobile;

    /**
     * 联系人电话
     */
    @Column(name = "contact_phone")
    private String contactPhone;

    /**
     * 联系人淘宝user_id
     */
    @Column(name = "taobao_user_id")
    private Long taobaoUserId;

    /**
     * 扩展属性
     */
    private String feature;

    /**
     * 物流站服务code
     */
    @Column(name = "service_code")
    private String serviceCode;

    /**
     * 物流站管理员淘宝nick，以；分割
     */
    private String managers;

    /**
     * 审批状态
     */
    private String state;

    /**
     * 菜鸟物流编号
     */
    @Column(name = "logistics_station_num")
    private String logisticsStationNum;

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
     * 获取省
     *
     * @return province - 省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省
     *
     * @param province 省
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取省名称
     *
     * @return province_detail - 省名称
     */
    public String getProvinceDetail() {
        return provinceDetail;
    }

    /**
     * 设置省名称
     *
     * @param provinceDetail 省名称
     */
    public void setProvinceDetail(String provinceDetail) {
        this.provinceDetail = provinceDetail;
    }

    /**
     * 获取市
     *
     * @return city - 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市
     *
     * @param city 市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取市名称
     *
     * @return city_detail - 市名称
     */
    public String getCityDetail() {
        return cityDetail;
    }

    /**
     * 设置市名称
     *
     * @param cityDetail 市名称
     */
    public void setCityDetail(String cityDetail) {
        this.cityDetail = cityDetail;
    }

    /**
     * 获取县
     *
     * @return county - 县
     */
    public String getCounty() {
        return county;
    }

    /**
     * 设置县
     *
     * @param county 县
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * 获取县名称
     *
     * @return county_detail - 县名称
     */
    public String getCountyDetail() {
        return countyDetail;
    }

    /**
     * 设置县名称
     *
     * @param countyDetail 县名称
     */
    public void setCountyDetail(String countyDetail) {
        this.countyDetail = countyDetail;
    }

    /**
     * 获取镇
     *
     * @return town - 镇
     */
    public String getTown() {
        return town;
    }

    /**
     * 设置镇
     *
     * @param town 镇
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * 获取镇名称
     *
     * @return town_detail - 镇名称
     */
    public String getTownDetail() {
        return townDetail;
    }

    /**
     * 设置镇名称
     *
     * @param townDetail 镇名称
     */
    public void setTownDetail(String townDetail) {
        this.townDetail = townDetail;
    }

    /**
     * 获取详细地址
     *
     * @return address_detail - 详细地址
     */
    public String getAddressDetail() {
        return addressDetail;
    }

    /**
     * 设置详细地址
     *
     * @param addressDetail 详细地址
     */
    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
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
     * 获取村
     *
     * @return village - 村
     */
    public String getVillage() {
        return village;
    }

    /**
     * 设置村
     *
     * @param village 村
     */
    public void setVillage(String village) {
        this.village = village;
    }

    /**
     * 获取村名称
     *
     * @return village_detail - 村名称
     */
    public String getVillageDetail() {
        return villageDetail;
    }

    /**
     * 设置村名称
     *
     * @param villageDetail 村名称
     */
    public void setVillageDetail(String villageDetail) {
        this.villageDetail = villageDetail;
    }

    /**
     * 获取菜鸟物流站id
     *
     * @return cainiao_station_id - 菜鸟物流站id
     */
    public Long getCainiaoStationId() {
        return cainiaoStationId;
    }

    /**
     * 设置菜鸟物流站id
     *
     * @param cainiaoStationId 菜鸟物流站id
     */
    public void setCainiaoStationId(Long cainiaoStationId) {
        this.cainiaoStationId = cainiaoStationId;
    }

    /**
     * 获取物流服务站名称
     *
     * @return name - 物流服务站名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置物流服务站名称
     *
     * @param name 物流服务站名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取联系人姓名
     *
     * @return contact_name - 联系人姓名
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置联系人姓名
     *
     * @param contactName 联系人姓名
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 获取上级物流站id
     *
     * @return parent_id - 上级物流站id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级物流站id
     *
     * @param parentId 上级物流站id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取物流站类型，3=县物流站点、4=村物流站点
     *
     * @return station_type - 物流站类型，3=县物流站点、4=村物流站点
     */
    public Integer getStationType() {
        return stationType;
    }

    /**
     * 设置物流站类型，3=县物流站点、4=村物流站点
     *
     * @param stationType 物流站类型，3=县物流站点、4=村物流站点
     */
    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    /**
     * 获取状态,村淘都是线下=-1
     *
     * @return status - 状态,村淘都是线下=-1
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态,村淘都是线下=-1
     *
     * @param status 状态,村淘都是线下=-1
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取联系人手机号码
     *
     * @return contact_mobile - 联系人手机号码
     */
    public String getContactMobile() {
        return contactMobile;
    }

    /**
     * 设置联系人手机号码
     *
     * @param contactMobile 联系人手机号码
     */
    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    /**
     * 获取联系人电话
     *
     * @return contact_phone - 联系人电话
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置联系人电话
     *
     * @param contactPhone 联系人电话
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * 获取联系人淘宝user_id
     *
     * @return taobao_user_id - 联系人淘宝user_id
     */
    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    /**
     * 设置联系人淘宝user_id
     *
     * @param taobaoUserId 联系人淘宝user_id
     */
    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    /**
     * 获取扩展属性
     *
     * @return feature - 扩展属性
     */
    public String getFeature() {
        return feature;
    }

    /**
     * 设置扩展属性
     *
     * @param feature 扩展属性
     */
    public void setFeature(String feature) {
        this.feature = feature;
    }

    /**
     * 获取物流站服务code
     *
     * @return service_code - 物流站服务code
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * 设置物流站服务code
     *
     * @param serviceCode 物流站服务code
     */
    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    /**
     * 获取物流站管理员淘宝nick，以；分割
     *
     * @return managers - 物流站管理员淘宝nick，以；分割
     */
    public String getManagers() {
        return managers;
    }

    /**
     * 设置物流站管理员淘宝nick，以；分割
     *
     * @param managers 物流站管理员淘宝nick，以；分割
     */
    public void setManagers(String managers) {
        this.managers = managers;
    }

    /**
     * 获取审批状态
     *
     * @return state - 审批状态
     */
    public String getState() {
        return state;
    }

    /**
     * 设置审批状态
     *
     * @param state 审批状态
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取菜鸟物流编号
     *
     * @return logistics_station_num - 菜鸟物流编号
     */
    public String getLogisticsStationNum() {
        return logisticsStationNum;
    }

    /**
     * 设置菜鸟物流编号
     *
     * @param logisticsStationNum 菜鸟物流编号
     */
    public void setLogisticsStationNum(String logisticsStationNum) {
        this.logisticsStationNum = logisticsStationNum;
    }
}