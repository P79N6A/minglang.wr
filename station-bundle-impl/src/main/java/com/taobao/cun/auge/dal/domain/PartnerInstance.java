package com.taobao.cun.auge.dal.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class PartnerInstance {

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
	 * 村服务站id
	 */
	@Column(name = "station_id")
	private Long stationId;

	/**
	 * 申请时间
	 */
	@Column(name = "apply_time")
	private Date applyTime;

	/**
	 * 服务开始时间
	 */
	@Column(name = "service_begin_time")
	private Date serviceBeginTime;

	/**
	 * 服务结束时间
	 */
	@Column(name = "service_end_time")
	private Date serviceEndTime;

	/**
	 * 合伙人id
	 */
	@Column(name = "partner_id")
	private Long partnerId;

	/**
	 * 淘帮手所属合伙人的村服务站id
	 */
	@Column(name = "parent_station_id")
	private Long parentStationId;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 申请人
	 */
	@Column(name = "applier_id")
	private String applierId;

	/**
	 * 位：可以用来标示淘帮手是否是由合伙人降级而来
	 */
	private Integer bit;

	/**
	 * 合伙人or村拍档
	 */
	private String type;

	/**
	 * 开业时间
	 */
	@Column(name = "open_date")
	private Date openDate;

	/**
	 * 是否是当前人
	 */
	@Column(name = "is_current")
	private String isCurrent;

	/**
	 * 申请人类型，buc，还是havana
	 */
	@Column(name = "applier_type")
	private String applierType;

	/**
	 * 停业类型：合伙人主动退出，还是小二主动清退
	 */
	@Column(name = "close_type")
	private String closeType;

	/**
	 * station_aply表主键，供数据迁移使用
	 */
	@Column(name = "station_apply_id")
	private Long stationApplyId;

	// ----------------人--------------------

	/**
	 * 姓名
	 */
	private String partnerName;

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
	private String partnerDescription;

	/**
	 * 状态：temp:暂存，normal:正常
	 */
	private String partnerState;

	// -------------村-----------------

	/**
	 * 服务站名称
	 */
	private String stationName;

	/**
	 * 服务站简介
	 */
	private String stationDescription;

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
	 * 村/社区名称 详情
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
	 * 场地固点类型 GOV_FIXED 政府固点 TRIPARTITE_FIXED 三方固点
	 */
	@Column(name = "fixed_type")
	private String fixedType;
	
	
	// -------------生命周期-----------------
	
	/**
	 * 生命周期业务类型
	 */
    private String lifecycleBusinessType;
    
    /**
     * 入驻协议
     */
    private String settledProtocol;
    
    /**
     * 保证金
     */
    private String bond;
    
    /**
     * 退出协议
     */
    private String quitProtocol;
    
    /**
     * 物流审批
     */
    private String logisticsApprove;
    
    /**
     * 当前步骤
     */
    private String currentStep;
    
    /**
     * 角色审批
     */
    private String roleApprove;
    
    /**
     * 小二确认
     */
    private String confirm;
    
    /**
     * 系统操作
     */
    private String system;
    
    /**
     * 县服务中心名称
     */
    private String countyStationName;

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
	 * @param id
	 *            主键
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
	 * @param gmtCreate
	 *            创建时间
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
	 * @param gmtModified
	 *            修改时间
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
	 * @param creator
	 *            创建者
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
	 * @param modifier
	 *            修改者
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
	 * @param isDeleted
	 *            是否已删除
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * 获取村服务站id
	 *
	 * @return station_id - 村服务站id
	 */
	public Long getStationId() {
		return stationId;
	}

	/**
	 * 设置村服务站id
	 *
	 * @param stationId
	 *            村服务站id
	 */
	public void setStationId(Long stationId) {
		this.stationId = stationId;
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
	 * @param applyTime
	 *            申请时间
	 */
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	/**
	 * 获取服务开始时间
	 *
	 * @return service_begin_time - 服务开始时间
	 */
	public Date getServiceBeginTime() {
		return serviceBeginTime;
	}

	/**
	 * 设置服务开始时间
	 *
	 * @param serviceBeginTime
	 *            服务开始时间
	 */
	public void setServiceBeginTime(Date serviceBeginTime) {
		this.serviceBeginTime = serviceBeginTime;
	}

	/**
	 * 获取服务结束时间
	 *
	 * @return service_end_time - 服务结束时间
	 */
	public Date getServiceEndTime() {
		return serviceEndTime;
	}

	/**
	 * 设置服务结束时间
	 *
	 * @param serviceEndTime
	 *            服务结束时间
	 */
	public void setServiceEndTime(Date serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}

	/**
	 * 获取合伙人id
	 *
	 * @return partner_id - 合伙人id
	 */
	public Long getPartnerId() {
		return partnerId;
	}

	/**
	 * 设置合伙人id
	 *
	 * @param partnerId
	 *            合伙人id
	 */
	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	/**
	 * 获取淘帮手所属合伙人的村服务站id
	 *
	 * @return parent_station_id - 淘帮手所属合伙人的村服务站id
	 */
	public Long getParentStationId() {
		return parentStationId;
	}

	/**
	 * 设置淘帮手所属合伙人的村服务站id
	 *
	 * @param parentStationId
	 *            淘帮手所属合伙人的村服务站id
	 */
	public void setParentStationId(Long parentStationId) {
		this.parentStationId = parentStationId;
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
	 * @param state
	 *            状态
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 获取申请人
	 *
	 * @return applier_id - 申请人
	 */
	public String getApplierId() {
		return applierId;
	}

	/**
	 * 设置申请人
	 *
	 * @param applierId
	 *            申请人
	 */
	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}

	/**
	 * 获取位：可以用来标示淘帮手是否是由合伙人降级而来
	 *
	 * @return bit - 位：可以用来标示淘帮手是否是由合伙人降级而来
	 */
	public Integer getBit() {
		return bit;
	}

	/**
	 * 设置位：可以用来标示淘帮手是否是由合伙人降级而来
	 *
	 * @param bit
	 *            位：可以用来标示淘帮手是否是由合伙人降级而来
	 */
	public void setBit(Integer bit) {
		this.bit = bit;
	}

	/**
	 * 获取合伙人or村拍档
	 *
	 * @return type - 合伙人or村拍档
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置合伙人or村拍档
	 *
	 * @param type
	 *            合伙人or村拍档
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取开业时间
	 *
	 * @return open_date - 开业时间
	 */
	public Date getOpenDate() {
		return openDate;
	}

	/**
	 * 设置开业时间
	 *
	 * @param openDate
	 *            开业时间
	 */
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	/**
	 * 获取是否是当前人
	 *
	 * @return is_current - 是否是当前人
	 */
	public String getIsCurrent() {
		return isCurrent;
	}

	/**
	 * 设置是否是当前人
	 *
	 * @param isCurrent
	 *            是否是当前人
	 */
	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
	}

	/**
	 * 获取申请人类型，buc，还是havana
	 *
	 * @return applier_type - 申请人类型，buc，还是havana
	 */
	public String getApplierType() {
		return applierType;
	}

	/**
	 * 设置申请人类型，buc，还是havana
	 *
	 * @param applierType
	 *            申请人类型，buc，还是havana
	 */
	public void setApplierType(String applierType) {
		this.applierType = applierType;
	}

	/**
	 * 获取停业类型：合伙人主动退出，还是小二主动清退
	 *
	 * @return close_type - 停业类型：合伙人主动退出，还是小二主动清退
	 */
	public String getCloseType() {
		return closeType;
	}

	/**
	 * 设置停业类型：合伙人主动退出，还是小二主动清退
	 *
	 * @param closeType
	 *            停业类型：合伙人主动退出，还是小二主动清退
	 */
	public void setCloseType(String closeType) {
		this.closeType = closeType;
	}

	/**
	 * 获取station_aply表主键，供数据迁移使用
	 *
	 * @return station_apply_id - station_aply表主键，供数据迁移使用
	 */
	public Long getStationApplyId() {
		return stationApplyId;
	}

	/**
	 * 设置station_aply表主键，供数据迁移使用
	 *
	 * @param stationApplyId
	 *            station_aply表主键，供数据迁移使用
	 */
	public void setStationApplyId(Long stationApplyId) {
		this.stationApplyId = stationApplyId;
	}

	/**
	 * 获取姓名
	 *
	 * @return name - 姓名
	 */

	public String getPartnerName() {
		return partnerName;
	}

	/**
	 * 设置姓名
	 *
	 * @param name
	 *            姓名
	 */
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
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
	 * @param alipayAccount
	 *            支付宝账号
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
	 * @param taobaoUserId
	 *            淘宝user_id
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
	 * @param taobaoNick
	 *            淘宝nick
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
	 * @param idenNum
	 *            身份证号码
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
	 * @param mobile
	 *            手机号码
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
	 * @param email
	 *            email
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
	 * @param businessType
	 *            经营类型全职： fulltime 兼职：partime
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	/**
	 * 获取现状描述
	 *
	 * @return description - 现状描述
	 */
	public String getPartnerDescription() {
		return partnerDescription;
	}

	/**
	 * 设置现状描述
	 *
	 * @param description
	 *            现状描述
	 */
	public void setPartnerDescription(String partnerDescription) {
		this.partnerDescription = partnerDescription;
	}

	/**
	 * 获取状态：temp:暂存，normal:正常
	 *
	 * @return state - 状态：temp:暂存，normal:正常
	 */
	public String getPartnerState() {
		return partnerState;
	}

	/**
	 * 设置状态：temp:暂存，normal:正常
	 *
	 * @param state
	 *            状态：temp:暂存，normal:正常
	 */
	public void setPartnerState(String partnerState) {
		this.partnerState = partnerState;
	}

	/**
	 * 获取服务站名称
	 *
	 * @return name - 服务站名称
	 */

	public String getStationName() {
		return stationName;
	}

	/**
	 * 设置服务站名称
	 *
	 * @param name
	 *            服务站名称
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	/**
	 * 获取服务站简介
	 *
	 * @return description - 服务站简介
	 */
	public String getStationDescription() {
		return stationDescription;
	}

	/**
	 * 设置服务站简介
	 *
	 * @param description
	 *            服务站简介
	 */
	public void setStationDescription(String stationDescription) {
		this.stationDescription = stationDescription;
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
	 * @param province
	 *            省编码
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
	 * @param city
	 *            市编码
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
	 * @param county
	 *            县/区编码
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
	 * @param town
	 *            乡镇编码
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
	 * @param provinceDetail
	 *            省详细
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
	 * @param cityDetail
	 *            市详细
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
	 * @param countyDetail
	 *            县/区详细
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
	 * @param townDetail
	 *            乡镇详细
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
	 * @param address
	 *            详细地址
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
	 * @param applyOrg
	 *            申请组织
	 */
	public void setApplyOrg(Long applyOrg) {
		this.applyOrg = applyOrg;
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
	 * @param stationNum
	 *            村点编号
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
	 * @param lng
	 *            经度
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
	 * @param lat
	 *            纬度
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
	 * @param village
	 *            村/社区编号
	 */
	public void setVillage(String village) {
		this.village = village;
	}

	/**
	 * 获取村/社区名称 详情
	 *
	 * @return village_detail - 村/社区名称 详情
	 */
	public String getVillageDetail() {
		return villageDetail;
	}

	/**
	 * 设置村/社区名称 详情
	 *
	 * @param villageDetail
	 *            村/社区名称 详情
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
	 * @param covered
	 *            覆盖人口
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
	 * @param products
	 *            特色农副产品
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
	 * @param logisticsState
	 *            物流状态
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
	 * @param format
	 *            目前业态
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
	 * @param areaType
	 *            固点，或者不固点
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
	 * @param managerId
	 *            管理员user_id
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
	 * @param providerId
	 *            服务商id
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
	 * @param feature
	 *            其他特性，用于扩展服务站属性
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
	 * @param status
	 *            新的服务站状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取场地固点类型 GOV_FIXED 政府固点 TRIPARTITE_FIXED 三方固点
	 *
	 * @return fixed_type - 场地固点类型 GOV_FIXED 政府固点 TRIPARTITE_FIXED 三方固点
	 */
	public String getFixedType() {
		return fixedType;
	}

	/**
	 * 设置场地固点类型 GOV_FIXED 政府固点 TRIPARTITE_FIXED 三方固点
	 *
	 * @param fixedType
	 *            场地固点类型 GOV_FIXED 政府固点 TRIPARTITE_FIXED 三方固点
	 */
	public void setFixedType(String fixedType) {
		this.fixedType = fixedType;
	}

	public String getLifecycleBusinessType() {
		return lifecycleBusinessType;
	}

	public void setLifecycleBusinessType(String lifecycleBusinessType) {
		this.lifecycleBusinessType = lifecycleBusinessType;
	}

	public String getSettledProtocol() {
		return settledProtocol;
	}

	public void setSettledProtocol(String settledProtocol) {
		this.settledProtocol = settledProtocol;
	}

	public String getBond() {
		return bond;
	}

	public void setBond(String bond) {
		this.bond = bond;
	}

	public String getQuitProtocol() {
		return quitProtocol;
	}

	public void setQuitProtocol(String quitProtocol) {
		this.quitProtocol = quitProtocol;
	}

	public String getLogisticsApprove() {
		return logisticsApprove;
	}

	public void setLogisticsApprove(String logisticsApprove) {
		this.logisticsApprove = logisticsApprove;
	}

	public String getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}

	public String getRoleApprove() {
		return roleApprove;
	}

	public void setRoleApprove(String roleApprove) {
		this.roleApprove = roleApprove;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public String getCountyStationName() {
		return countyStationName;
	}

	public void setCountyStationName(String countyStationName) {
		this.countyStationName = countyStationName;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
}
