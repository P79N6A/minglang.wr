package com.taobao.cun.auge.logistics.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 物流申请单
 * @author zhenhuan.zhangzh
 *
 */
public class LogisticsStationApplyDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	/**
	 * 申请人stationId
	 */
	private Long applierStationId;

	/**
	 * 地址库-省id NotNull
	 */
	private Long provinceId;

	/**
	 * 省名称
	 */
	private String province;
	/**
	 * 地址库-城id
	 */
	private Long cityId;

	/**
	 * 市名称
	 */
	private String city;
	/**
	 * 地址库-县id
	 */
	private Long countyId;

	/**
	 * 县名称
	 */
	private String county;

	/**
	 * 地址库-镇id
	 */
	private Long townId;

	/**
	 * 镇名称
	 */
	private String town;

	/**
	 * 地址库-村id
	 */
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
	 * 淘帮手名字
	 */
	private String applierName;

	/**
	 * 淘帮手手机号
	 */
	private String mobile;

	/**
	 * 合伙人taobaoUserId
	 */
	private Long taobaoUserId;

	/**
	 * 合伙人的StationId
	 */
	private Long stationId;

	/**
	 * 申请人的淘宝userId
	 */
	private Long applierTaobaoUserId;

	/**
	 * ctpType
	 */
	private String ctpType;
	
	/**
	 * 申请人的组织ID
	 */
	private Long applierOrgId;
	
	/**
	 *
	 */
	private String applyStatus;
	
	/**
	 * 
	 */
	private String remark;
	
	/**
	 * 
	 */
	private Date gmtCreate;
	
	private String type;
	
	private LogisticsStationDto logisticStationDto;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApplierStationId() {
		return applierStationId;
	}

	public void setApplierStationId(Long applierStationId) {
		this.applierStationId = applierStationId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Long getTownId() {
		return townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getApplierTaobaoUserId() {
		return applierTaobaoUserId;
	}

	public void setApplierTaobaoUserId(Long applierTaobaoUserId) {
		this.applierTaobaoUserId = applierTaobaoUserId;
	}

	public String getCtpType() {
		return ctpType;
	}

	public void setCtpType(String ctpType) {
		this.ctpType = ctpType;
	}

	public Long getApplierOrgId() {
		return applierOrgId;
	}

	public void setApplierOrgId(Long applierOrgId) {
		this.applierOrgId = applierOrgId;
	}


	public LogisticsStationDto getLogisticStationDto() {
		return logisticStationDto;
	}

	public void setLogisticStationDto(LogisticsStationDto logisticStationDto) {
		this.logisticStationDto = logisticStationDto;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
