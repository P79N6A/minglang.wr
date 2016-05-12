package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "station_apply")
public class StationApply {
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
     * 服务点名称
     */
    private String name;

    /**
     * 服务点状态
     */
    private String state;

    /**
     * 申请者名称
     */
    @Column(name = "applier_name")
    private String applierName;

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
     * 现状描述
     */
    private String description;

    /**
     * 目前业态
     */
    private String format;

    /**
     * 支付宝账号
     */
    @Column(name = "alipay_account")
    private String alipayAccount;

    /**
     * 淘宝登录账号
     */
    @Column(name = "taobao_nick")
    private String taobaoNick;

    /**
     * 服务站点id,station表外键
     */
    @Column(name = "station_id")
    private Long stationId;

    /**
     * 所属组织,station_org表外键
     */
    @Column(name = "own_org_id")
    private Long ownOrgId;

    /**
     * 服务开始时间
     */
    @Column(name = "service_begin_date")
    private Date serviceBeginDate;

    /**
     * 服务结束时间
     */
    @Column(name = "service_end_date")
    private Date serviceEndDate;

    /**
     * 申请时间
     */
    @Column(name = "apply_time")
    private Date applyTime;

    /**
     * 版本号
     */
    private Long version;

    /**
     * email
     */
    private String email;

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
     * 用户确认时间
     */
    @Column(name = "confirmed_time")
    private Date confirmedTime;

    /**
     * 用户协议确认子阶段
     */
    @Column(name = "protocol_confirming_step")
    private String protocolConfirmingStep;

    /**
     * 冻结时间
     */
    @Column(name = "frozen_time")
    private Date frozenTime;

    /**
     * 淘宝user_id
     */
    @Column(name = "taobao_user_id")
    private Long taobaoUserId;

    /**
     * 申请人类型(BUC或者HAVANA)
     */
    @Column(name = "applier_type")
    private String applierType;

    /**
     * 申请人id
     */
    @Column(name = "applier_id")
    private String applierId;

    /**
     * 提交者姓名
     */
    @Column(name = "submitted_people_name")
    private String submittedPeopleName;

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
     * 村/社区编码
     */
    private String village;

    /**
     * 村/社区名称
     */
    @Column(name = "village_detail")
    private String villageDetail;

    /**
     * 解冻时间
     */
    @Column(name = "thaw_time")
    private Date thawTime;

    /**
     * 协议的版本
     */
    @Column(name = "protocol_version")
    private String protocolVersion;

    /**
     * 冻结资金
     */
    @Column(name = "frozen_money")
    private String frozenMoney;

    /**
     * 计划开业时间
     */
    @Column(name = "open_date")
    private Date openDate;

    /**
     * 村点退出时确认的协议版本
     */
    @Column(name = "quit_protocol_version")
    private String quitProtocolVersion;

    /**
     * 村点退出的类型，代购员主动退出或者小二清退等
     */
    @Column(name = "quit_type")
    private String quitType;

    /**
     * 最近拜访时间
     */
    @Column(name = "contact_date")
    private Date contactDate;

    /**
     * 客户贷款额度
     */
    @Column(name = "customer_level")
    private Double customerLevel;

    /**
     * 场地类型fix：固定 free：不固定
     */
    @Column(name = "area_type")
    private String areaType;

    /**
     * 经营类型全职： fulltime 兼职：partime
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * 管理协议版本
     */
    @Column(name = "manage_protocol_version")
    private String manageProtocolVersion;

    /**
     * 管理协议确认时间
     */
    @Column(name = "manage_protocol_confirmed")
    private Date manageProtocolConfirmed;

    /**
     * 经营者类型（合伙人，淘帮手）
     */
    @Column(name = "operator_type")
    private String operatorType;

    /**
     * 淘帮手对应合伙人station_id
     */
    @Column(name = "partner_station_id")
    private Long partnerStationId;

    /**
     * 场地固点类型 GOV_FIXED 政府固点
TRIPARTITE_FIXED
三方固点
     */
    @Column(name = "fixed_type")
    private String fixedType;

    /**
     * 是否本村
     */
    @Column(name = "village_flag")
    private String villageFlag;

    /**
     * 是否有经营场地
     */
    @Column(name = "place_flag")
    private String placeFlag;

    /**
     * 淘帮手行业类型
     */
    private String category;

    /**
     * 经营类型
     */
    @Column(name = "management_type")
    private String managementType;

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
     * 获取服务点名称
     *
     * @return name - 服务点名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置服务点名称
     *
     * @param name 服务点名称
     */
    public void setName(String name) {
        this.name = name;
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
     * 获取申请者名称
     *
     * @return applier_name - 申请者名称
     */
    public String getApplierName() {
        return applierName;
    }

    /**
     * 设置申请者名称
     *
     * @param applierName 申请者名称
     */
    public void setApplierName(String applierName) {
        this.applierName = applierName;
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
     * 获取淘宝登录账号
     *
     * @return taobao_nick - 淘宝登录账号
     */
    public String getTaobaoNick() {
        return taobaoNick;
    }

    /**
     * 设置淘宝登录账号
     *
     * @param taobaoNick 淘宝登录账号
     */
    public void setTaobaoNick(String taobaoNick) {
        this.taobaoNick = taobaoNick;
    }

    /**
     * 获取服务站点id,station表外键
     *
     * @return station_id - 服务站点id,station表外键
     */
    public Long getStationId() {
        return stationId;
    }

    /**
     * 设置服务站点id,station表外键
     *
     * @param stationId 服务站点id,station表外键
     */
    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    /**
     * 获取所属组织,station_org表外键
     *
     * @return own_org_id - 所属组织,station_org表外键
     */
    public Long getOwnOrgId() {
        return ownOrgId;
    }

    /**
     * 设置所属组织,station_org表外键
     *
     * @param ownOrgId 所属组织,station_org表外键
     */
    public void setOwnOrgId(Long ownOrgId) {
        this.ownOrgId = ownOrgId;
    }

    /**
     * 获取服务开始时间
     *
     * @return service_begin_date - 服务开始时间
     */
    public Date getServiceBeginDate() {
        return serviceBeginDate;
    }

    /**
     * 设置服务开始时间
     *
     * @param serviceBeginDate 服务开始时间
     */
    public void setServiceBeginDate(Date serviceBeginDate) {
        this.serviceBeginDate = serviceBeginDate;
    }

    /**
     * 获取服务结束时间
     *
     * @return service_end_date - 服务结束时间
     */
    public Date getServiceEndDate() {
        return serviceEndDate;
    }

    /**
     * 设置服务结束时间
     *
     * @param serviceEndDate 服务结束时间
     */
    public void setServiceEndDate(Date serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    /**
     * 获取申请时间
     *
     * @return apply_time - 申请时间
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * 设置申请时间
     *
     * @param applyTime 申请时间
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * 获取版本号
     *
     * @return version - 版本号
     */
    public Long getVersion() {
        return version;
    }

    /**
     * 设置版本号
     *
     * @param version 版本号
     */
    public void setVersion(Long version) {
        this.version = version;
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
     * 获取用户确认时间
     *
     * @return confirmed_time - 用户确认时间
     */
    public Date getConfirmedTime() {
        return confirmedTime;
    }

    /**
     * 设置用户确认时间
     *
     * @param confirmedTime 用户确认时间
     */
    public void setConfirmedTime(Date confirmedTime) {
        this.confirmedTime = confirmedTime;
    }

    /**
     * 获取用户协议确认子阶段
     *
     * @return protocol_confirming_step - 用户协议确认子阶段
     */
    public String getProtocolConfirmingStep() {
        return protocolConfirmingStep;
    }

    /**
     * 设置用户协议确认子阶段
     *
     * @param protocolConfirmingStep 用户协议确认子阶段
     */
    public void setProtocolConfirmingStep(String protocolConfirmingStep) {
        this.protocolConfirmingStep = protocolConfirmingStep;
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
     * 获取申请人类型(BUC或者HAVANA)
     *
     * @return applier_type - 申请人类型(BUC或者HAVANA)
     */
    public String getApplierType() {
        return applierType;
    }

    /**
     * 设置申请人类型(BUC或者HAVANA)
     *
     * @param applierType 申请人类型(BUC或者HAVANA)
     */
    public void setApplierType(String applierType) {
        this.applierType = applierType;
    }

    /**
     * 获取申请人id
     *
     * @return applier_id - 申请人id
     */
    public String getApplierId() {
        return applierId;
    }

    /**
     * 设置申请人id
     *
     * @param applierId 申请人id
     */
    public void setApplierId(String applierId) {
        this.applierId = applierId;
    }

    /**
     * 获取提交者姓名
     *
     * @return submitted_people_name - 提交者姓名
     */
    public String getSubmittedPeopleName() {
        return submittedPeopleName;
    }

    /**
     * 设置提交者姓名
     *
     * @param submittedPeopleName 提交者姓名
     */
    public void setSubmittedPeopleName(String submittedPeopleName) {
        this.submittedPeopleName = submittedPeopleName;
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
     * 获取村/社区编码
     *
     * @return village - 村/社区编码
     */
    public String getVillage() {
        return village;
    }

    /**
     * 设置村/社区编码
     *
     * @param village 村/社区编码
     */
    public void setVillage(String village) {
        this.village = village;
    }

    /**
     * 获取村/社区名称
     *
     * @return village_detail - 村/社区名称
     */
    public String getVillageDetail() {
        return villageDetail;
    }

    /**
     * 设置村/社区名称
     *
     * @param villageDetail 村/社区名称
     */
    public void setVillageDetail(String villageDetail) {
        this.villageDetail = villageDetail;
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
     * 获取协议的版本
     *
     * @return protocol_version - 协议的版本
     */
    public String getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * 设置协议的版本
     *
     * @param protocolVersion 协议的版本
     */
    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * 获取冻结资金
     *
     * @return frozen_money - 冻结资金
     */
    public String getFrozenMoney() {
        return frozenMoney;
    }

    /**
     * 设置冻结资金
     *
     * @param frozenMoney 冻结资金
     */
    public void setFrozenMoney(String frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    /**
     * 获取计划开业时间
     *
     * @return open_date - 计划开业时间
     */
    public Date getOpenDate() {
        return openDate;
    }

    /**
     * 设置计划开业时间
     *
     * @param openDate 计划开业时间
     */
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    /**
     * 获取村点退出时确认的协议版本
     *
     * @return quit_protocol_version - 村点退出时确认的协议版本
     */
    public String getQuitProtocolVersion() {
        return quitProtocolVersion;
    }

    /**
     * 设置村点退出时确认的协议版本
     *
     * @param quitProtocolVersion 村点退出时确认的协议版本
     */
    public void setQuitProtocolVersion(String quitProtocolVersion) {
        this.quitProtocolVersion = quitProtocolVersion;
    }

    /**
     * 获取村点退出的类型，代购员主动退出或者小二清退等
     *
     * @return quit_type - 村点退出的类型，代购员主动退出或者小二清退等
     */
    public String getQuitType() {
        return quitType;
    }

    /**
     * 设置村点退出的类型，代购员主动退出或者小二清退等
     *
     * @param quitType 村点退出的类型，代购员主动退出或者小二清退等
     */
    public void setQuitType(String quitType) {
        this.quitType = quitType;
    }

    /**
     * 获取最近拜访时间
     *
     * @return contact_date - 最近拜访时间
     */
    public Date getContactDate() {
        return contactDate;
    }

    /**
     * 设置最近拜访时间
     *
     * @param contactDate 最近拜访时间
     */
    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    /**
     * 获取客户贷款额度
     *
     * @return customer_level - 客户贷款额度
     */
    public Double getCustomerLevel() {
        return customerLevel;
    }

    /**
     * 设置客户贷款额度
     *
     * @param customerLevel 客户贷款额度
     */
    public void setCustomerLevel(Double customerLevel) {
        this.customerLevel = customerLevel;
    }

    /**
     * 获取场地类型fix：固定 free：不固定
     *
     * @return area_type - 场地类型fix：固定 free：不固定
     */
    public String getAreaType() {
        return areaType;
    }

    /**
     * 设置场地类型fix：固定 free：不固定
     *
     * @param areaType 场地类型fix：固定 free：不固定
     */
    public void setAreaType(String areaType) {
        this.areaType = areaType;
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
     * 获取管理协议版本
     *
     * @return manage_protocol_version - 管理协议版本
     */
    public String getManageProtocolVersion() {
        return manageProtocolVersion;
    }

    /**
     * 设置管理协议版本
     *
     * @param manageProtocolVersion 管理协议版本
     */
    public void setManageProtocolVersion(String manageProtocolVersion) {
        this.manageProtocolVersion = manageProtocolVersion;
    }

    /**
     * 获取管理协议确认时间
     *
     * @return manage_protocol_confirmed - 管理协议确认时间
     */
    public Date getManageProtocolConfirmed() {
        return manageProtocolConfirmed;
    }

    /**
     * 设置管理协议确认时间
     *
     * @param manageProtocolConfirmed 管理协议确认时间
     */
    public void setManageProtocolConfirmed(Date manageProtocolConfirmed) {
        this.manageProtocolConfirmed = manageProtocolConfirmed;
    }

    /**
     * 获取经营者类型（合伙人，淘帮手）
     *
     * @return operator_type - 经营者类型（合伙人，淘帮手）
     */
    public String getOperatorType() {
        return operatorType;
    }

    /**
     * 设置经营者类型（合伙人，淘帮手）
     *
     * @param operatorType 经营者类型（合伙人，淘帮手）
     */
    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    /**
     * 获取淘帮手对应合伙人station_id
     *
     * @return partner_station_id - 淘帮手对应合伙人station_id
     */
    public Long getPartnerStationId() {
        return partnerStationId;
    }

    /**
     * 设置淘帮手对应合伙人station_id
     *
     * @param partnerStationId 淘帮手对应合伙人station_id
     */
    public void setPartnerStationId(Long partnerStationId) {
        this.partnerStationId = partnerStationId;
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

    /**
     * 获取是否本村
     *
     * @return village_flag - 是否本村
     */
    public String getVillageFlag() {
        return villageFlag;
    }

    /**
     * 设置是否本村
     *
     * @param villageFlag 是否本村
     */
    public void setVillageFlag(String villageFlag) {
        this.villageFlag = villageFlag;
    }

    /**
     * 获取是否有经营场地
     *
     * @return place_flag - 是否有经营场地
     */
    public String getPlaceFlag() {
        return placeFlag;
    }

    /**
     * 设置是否有经营场地
     *
     * @param placeFlag 是否有经营场地
     */
    public void setPlaceFlag(String placeFlag) {
        this.placeFlag = placeFlag;
    }

    /**
     * 获取淘帮手行业类型
     *
     * @return category - 淘帮手行业类型
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置淘帮手行业类型
     *
     * @param category 淘帮手行业类型
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 获取经营类型
     *
     * @return management_type - 经营类型
     */
    public String getManagementType() {
        return managementType;
    }

    /**
     * 设置经营类型
     *
     * @param managementType 经营类型
     */
    public void setManagementType(String managementType) {
        this.managementType = managementType;
    }
}