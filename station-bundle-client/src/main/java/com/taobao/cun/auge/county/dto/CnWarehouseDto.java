package com.taobao.cun.auge.county.dto;

import java.io.Serializable;

public class CnWarehouseDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 县仓ID
	 */
	private Long id;
	/**
	 * 县仓名称
	 */
	private String name;
	/**
	 * 详细地址
	 */
	private String address;
	/**
	 * 省编码
	 */
	private Long provinceId;
	/**
	 * 市编码
	 */
	private Long cityId;
	/**
	 * 县编码
	 */
	private Long countyId;
	/**
	 * 镇编码
	 */
	private Long townId;
	/**
	 * 对应资源中心的编码
	 */
	private String resCode;
	/**
	 * 详细描述
	 */
	private String description;
	/**
	 * 经度
	 */
	private String lng;
	/**
	 * 纬度
	 */
	private String lat;
	/**
	 * 营业开始时间, 0点后的秒数
	 */
	private Long bizStart;
	/**
	 * 营业结束时间, 0点后的秒数
	 */
	private Long bizEnd;
	/**
	 * 邮编
	 */
	private String zipCode;
	/**
	 * 县域ID
	 */
	private Long countyDomainId;
	/**
	 * 关联村站数
	 */
	private Integer stationCount;
	/**
	 * 菜鸟运营小二
	 */
	private String cnUserName;
	/**
	 * 站长姓名
	 */
	private String whOwnerName;
	/**
	 * 站长手机号
	 */
	private String whOwnerMobile;
	/**
	 * 站长邮箱
	 */
	private String whOwnerEmail;
	/**
	 * 县仓code
	 */
	private String ctCode;
	/**
	 * 状态
	 */
	private String warehouseStatus;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
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
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public Long getBizStart() {
		return bizStart;
	}
	public void setBizStart(Long bizStart) {
		this.bizStart = bizStart;
	}
	public Long getBizEnd() {
		return bizEnd;
	}
	public void setBizEnd(Long bizEnd) {
		this.bizEnd = bizEnd;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public Long getCountyDomainId() {
		return countyDomainId;
	}
	public void setCountyDomainId(Long countyDomainId) {
		this.countyDomainId = countyDomainId;
	}
	public Integer getStationCount() {
		return stationCount;
	}
	public void setStationCount(Integer stationCount) {
		this.stationCount = stationCount;
	}
	public String getCnUserName() {
		return cnUserName;
	}
	public void setCnUserName(String cnUserName) {
		this.cnUserName = cnUserName;
	}
	public String getWhOwnerName() {
		return whOwnerName;
	}
	public void setWhOwnerName(String whOwnerName) {
		this.whOwnerName = whOwnerName;
	}
	public String getWhOwnerMobile() {
		return whOwnerMobile;
	}
	public void setWhOwnerMobile(String whOwnerMobile) {
		this.whOwnerMobile = whOwnerMobile;
	}
	public String getWhOwnerEmail() {
		return whOwnerEmail;
	}
	public void setWhOwnerEmail(String whOwnerEmail) {
		this.whOwnerEmail = whOwnerEmail;
	}
	public String getCtCode() {
		return ctCode;
	}
	public void setCtCode(String ctCode) {
		this.ctCode = ctCode;
	}
	public String getWarehouseStatus() {
		return warehouseStatus;
	}
	public void setWarehouseStatus(String warehouseStatus) {
		this.warehouseStatus = warehouseStatus;
	}
	
	
}
