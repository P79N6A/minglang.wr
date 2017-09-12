package com.taobao.cun.auge.logistics.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class LogisticsApplyRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 申请人stationId
	 */
	@NotNull(message="applierStationId not null")
	private Long applierStationId;
	
	/**
	 * 地址库-省id
	 * NotNull
	 */
	@NotNull(message="provinceId not null")
	private Long provinceId;

	/**
	 * 省名称
	 */
	@NotEmpty(message="province not empty")
	@NotNull(message="province not null")
	private String province;
	/**
	 * 地址库-城id
	 */
	@NotNull(message="cityId not null")
	private Long cityId;

	/**
	 * 市名称
	 */
	@NotEmpty(message="city not empty")
	@NotNull(message="city not null")
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
	@NotEmpty(message="address not empty")
	@NotNull(message="address not null")
	private String address;

	/**
	 * 淘帮手名字
	 */
	@NotEmpty(message="applierName not empty")
	@NotNull(message="applierName not null")
	private String applierName;
	
	/**
	 * 淘帮手手机号
	 */
	@NotNull(message="mobile not null")
	private String mobile;
	
	/**
	 * 合伙人taobaoUserId
	 */
	@NotNull(message="taobaoUserId not null")
	private Long taobaoUserId;
	
	/**
	 * 合伙人的StationId
	 */
	@NotNull(message="stationId not null")
	private Long stationId;
	
	/**
	 * 申请人的淘宝userId
	 */
	@NotNull(message="applierTaobaoUserId not null")
	private Long applierTaobaoUserId;
	
	/**
	 * ctpType
	 */
	@NotEmpty(message="ctpType not empty")
	@NotNull(message="ctpType not null")
	private String ctpType;
	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public Long getTownId() {
		return townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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


	public String getCtpType() {
		return ctpType;
	}

	public void setCtpType(String ctpType) {
		this.ctpType = ctpType;
	}

	public Long getApplierStationId() {
		return applierStationId;
	}

	public void setApplierStationId(Long applierStationId) {
		this.applierStationId = applierStationId;
	}

	public Long getApplierTaobaoUserId() {
		return applierTaobaoUserId;
	}

	public void setApplierTaobaoUserId(Long applierTaobaoUserId) {
		this.applierTaobaoUserId = applierTaobaoUserId;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	
	
}
