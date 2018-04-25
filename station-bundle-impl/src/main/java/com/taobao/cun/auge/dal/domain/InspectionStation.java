package com.taobao.cun.auge.dal.domain;

import javax.persistence.Column;

public class InspectionStation {

	/**
	 * 村服务站id
	 */
	@Column(name = "station_id")
	private Long stationId;

	
	/**
	 * 合伙人id
	 */
	@Column(name = "partner_id")
	private Long partnerId;


	/**
	 * 状态
	 */
	private String state;

	/**
	 * 合伙人or村拍档
	 */
	private String type;
	
	/**
	 * 姓名
	 */
	private String partnerName;

	
	/**
	 * 手机号码
	 */
	private String mobile;

	
	/**
	 * 服务站名称
	 */
	private String stationName;

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
	 * 县服务中心名称
	 */
	private String countyStationName;
	
	private String mode;
	

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

	public String getCountyStationName() {
		return countyStationName;
	}

	public void setCountyStationName(String countyStationName) {
		this.countyStationName = countyStationName;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

   
}
