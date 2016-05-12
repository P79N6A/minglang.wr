package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "county_station")
public class CountyStation {
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
     * 县运营中心名称
     */
    private String name;

    /**
     * 省
     */
    private String province;

    /**
     * 省详情
     */
    @Column(name = "province_detail")
    private String provinceDetail;

    /**
     * 市
     */
    private String city;

    /**
     * 市详情
     */
    @Column(name = "city_detail")
    private String cityDetail;

    /**
     * 县
     */
    private String county;

    /**
     * 县详情
     */
    @Column(name = "county_detail")
    private String countyDetail;

    /**
     * 乡
     */
    private String town;

    /**
     * 乡详情
     */
    @Column(name = "town_detail")
    private String townDetail;

    /**
     * 详细地址
     */
    @Column(name = "address_detail")
    private String addressDetail;

    /**
     * 组织id
     */
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 工号
     */
    @Column(name = "employee_id")
    private String employeeId;

    /**
     * 淘宝userid
     */
    @Column(name = "taobao_user_id")
    private Long taobaoUserId;

    /**
     * 经营状态：PLANNING（筹划中）、OPERATING（运营中）、STOP_OPERATION（停止运营）
     */
    @Column(name = "manage_status")
    private String manageStatus;

    /**
     * 服务中心面积
     */
    private String acreage;

    /**
     * 仓库数
     */
    @Column(name = "warehouse_num")
    private Integer warehouseNum;

    /**
     * 服务中心租赁模式
     */
    @Column(name = "leasing_model")
    private String leasingModel;

    /**
     * 办公地情况
     */
    @Column(name = "office_detail")
    private String officeDetail;

    /**
     * 免租期限
     */
    @Column(name = "free_deadline")
    private String freeDeadline;

    /**
     * 需自理费用
     */
    @Column(name = "self_costs")
    private String selfCosts;

    /**
     * 备注
     */
    private String remark;

    /**
     * 经营模式：SELF（自营）、THIRD_PARTY（第三方）
     */
    @Column(name = "manage_model")
    private String manageModel;

    /**
     * 淘宝昵称
     */
    @Column(name = "taobao_nick")
    private String taobaoNick;

    /**
     * 县运营中心的父组织ID，即大区组织id
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 联系电话
     */
    @Column(name = "employee_phone")
    private String employeePhone;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 物流联系电话
     */
    @Column(name = "logistics_phone")
    private String logisticsPhone;

    /**
     * 物流操作人
     */
    @Column(name = "logistics_operator")
    private String logisticsOperator;

    /**
     * 开始运营时间
     */
    @Column(name = "gmt_start_operation")
    private Date gmtStartOperation;

    /**
     * 其他特性
     */
    private String feature;

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
     * 获取县运营中心名称
     *
     * @return name - 县运营中心名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置县运营中心名称
     *
     * @param name 县运营中心名称
     */
    public void setName(String name) {
        this.name = name;
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
     * 获取省详情
     *
     * @return province_detail - 省详情
     */
    public String getProvinceDetail() {
        return provinceDetail;
    }

    /**
     * 设置省详情
     *
     * @param provinceDetail 省详情
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
     * 获取市详情
     *
     * @return city_detail - 市详情
     */
    public String getCityDetail() {
        return cityDetail;
    }

    /**
     * 设置市详情
     *
     * @param cityDetail 市详情
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
     * 获取县详情
     *
     * @return county_detail - 县详情
     */
    public String getCountyDetail() {
        return countyDetail;
    }

    /**
     * 设置县详情
     *
     * @param countyDetail 县详情
     */
    public void setCountyDetail(String countyDetail) {
        this.countyDetail = countyDetail;
    }

    /**
     * 获取乡
     *
     * @return town - 乡
     */
    public String getTown() {
        return town;
    }

    /**
     * 设置乡
     *
     * @param town 乡
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * 获取乡详情
     *
     * @return town_detail - 乡详情
     */
    public String getTownDetail() {
        return townDetail;
    }

    /**
     * 设置乡详情
     *
     * @param townDetail 乡详情
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
     * 获取组织id
     *
     * @return org_id - 组织id
     */
    public Long getOrgId() {
        return orgId;
    }

    /**
     * 设置组织id
     *
     * @param orgId 组织id
     */
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    /**
     * 获取工号
     *
     * @return employee_id - 工号
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * 设置工号
     *
     * @param employeeId 工号
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * 获取淘宝userid
     *
     * @return taobao_user_id - 淘宝userid
     */
    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    /**
     * 设置淘宝userid
     *
     * @param taobaoUserId 淘宝userid
     */
    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    /**
     * 获取经营状态：PLANNING（筹划中）、OPERATING（运营中）、STOP_OPERATION（停止运营）
     *
     * @return manage_status - 经营状态：PLANNING（筹划中）、OPERATING（运营中）、STOP_OPERATION（停止运营）
     */
    public String getManageStatus() {
        return manageStatus;
    }

    /**
     * 设置经营状态：PLANNING（筹划中）、OPERATING（运营中）、STOP_OPERATION（停止运营）
     *
     * @param manageStatus 经营状态：PLANNING（筹划中）、OPERATING（运营中）、STOP_OPERATION（停止运营）
     */
    public void setManageStatus(String manageStatus) {
        this.manageStatus = manageStatus;
    }

    /**
     * 获取服务中心面积
     *
     * @return acreage - 服务中心面积
     */
    public String getAcreage() {
        return acreage;
    }

    /**
     * 设置服务中心面积
     *
     * @param acreage 服务中心面积
     */
    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    /**
     * 获取仓库数
     *
     * @return warehouse_num - 仓库数
     */
    public Integer getWarehouseNum() {
        return warehouseNum;
    }

    /**
     * 设置仓库数
     *
     * @param warehouseNum 仓库数
     */
    public void setWarehouseNum(Integer warehouseNum) {
        this.warehouseNum = warehouseNum;
    }

    /**
     * 获取服务中心租赁模式
     *
     * @return leasing_model - 服务中心租赁模式
     */
    public String getLeasingModel() {
        return leasingModel;
    }

    /**
     * 设置服务中心租赁模式
     *
     * @param leasingModel 服务中心租赁模式
     */
    public void setLeasingModel(String leasingModel) {
        this.leasingModel = leasingModel;
    }

    /**
     * 获取办公地情况
     *
     * @return office_detail - 办公地情况
     */
    public String getOfficeDetail() {
        return officeDetail;
    }

    /**
     * 设置办公地情况
     *
     * @param officeDetail 办公地情况
     */
    public void setOfficeDetail(String officeDetail) {
        this.officeDetail = officeDetail;
    }

    /**
     * 获取免租期限
     *
     * @return free_deadline - 免租期限
     */
    public String getFreeDeadline() {
        return freeDeadline;
    }

    /**
     * 设置免租期限
     *
     * @param freeDeadline 免租期限
     */
    public void setFreeDeadline(String freeDeadline) {
        this.freeDeadline = freeDeadline;
    }

    /**
     * 获取需自理费用
     *
     * @return self_costs - 需自理费用
     */
    public String getSelfCosts() {
        return selfCosts;
    }

    /**
     * 设置需自理费用
     *
     * @param selfCosts 需自理费用
     */
    public void setSelfCosts(String selfCosts) {
        this.selfCosts = selfCosts;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取经营模式：SELF（自营）、THIRD_PARTY（第三方）
     *
     * @return manage_model - 经营模式：SELF（自营）、THIRD_PARTY（第三方）
     */
    public String getManageModel() {
        return manageModel;
    }

    /**
     * 设置经营模式：SELF（自营）、THIRD_PARTY（第三方）
     *
     * @param manageModel 经营模式：SELF（自营）、THIRD_PARTY（第三方）
     */
    public void setManageModel(String manageModel) {
        this.manageModel = manageModel;
    }

    /**
     * 获取淘宝昵称
     *
     * @return taobao_nick - 淘宝昵称
     */
    public String getTaobaoNick() {
        return taobaoNick;
    }

    /**
     * 设置淘宝昵称
     *
     * @param taobaoNick 淘宝昵称
     */
    public void setTaobaoNick(String taobaoNick) {
        this.taobaoNick = taobaoNick;
    }

    /**
     * 获取县运营中心的父组织ID，即大区组织id
     *
     * @return parent_id - 县运营中心的父组织ID，即大区组织id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置县运营中心的父组织ID，即大区组织id
     *
     * @param parentId 县运营中心的父组织ID，即大区组织id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取联系电话
     *
     * @return employee_phone - 联系电话
     */
    public String getEmployeePhone() {
        return employeePhone;
    }

    /**
     * 设置联系电话
     *
     * @param employeePhone 联系电话
     */
    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
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
     * 获取物流联系电话
     *
     * @return logistics_phone - 物流联系电话
     */
    public String getLogisticsPhone() {
        return logisticsPhone;
    }

    /**
     * 设置物流联系电话
     *
     * @param logisticsPhone 物流联系电话
     */
    public void setLogisticsPhone(String logisticsPhone) {
        this.logisticsPhone = logisticsPhone;
    }

    /**
     * 获取物流操作人
     *
     * @return logistics_operator - 物流操作人
     */
    public String getLogisticsOperator() {
        return logisticsOperator;
    }

    /**
     * 设置物流操作人
     *
     * @param logisticsOperator 物流操作人
     */
    public void setLogisticsOperator(String logisticsOperator) {
        this.logisticsOperator = logisticsOperator;
    }

    /**
     * 获取开始运营时间
     *
     * @return gmt_start_operation - 开始运营时间
     */
    public Date getGmtStartOperation() {
        return gmtStartOperation;
    }

    /**
     * 设置开始运营时间
     *
     * @param gmtStartOperation 开始运营时间
     */
    public void setGmtStartOperation(Date gmtStartOperation) {
        this.gmtStartOperation = gmtStartOperation;
    }

    /**
     * 获取其他特性
     *
     * @return feature - 其他特性
     */
    public String getFeature() {
        return feature;
    }

    /**
     * 设置其他特性
     *
     * @param feature 其他特性
     */
    public void setFeature(String feature) {
        this.feature = feature;
    }
}