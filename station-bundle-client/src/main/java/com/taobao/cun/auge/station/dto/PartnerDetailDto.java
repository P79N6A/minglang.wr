package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;
/**
 * 合伙人详情展现信息，提供给icuntao个人信息页面
 * @author yi.shaoy
 *
 */
public class PartnerDetailDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private Long taobaoUserId;
	private String province;
	private String city;
	private String town;
	private String county;
	private String countyCode;
	private String townCode;
	private String cityCode;
	private String provinceCode;
	private String addressDetail;
	private Date gmtServiceBegin;
	private String stationName;
	private String countyName;
	private String flowerName;
	private String idenNum;
	private String email;
	private String mobile;
	private String aliPay;
	private String taobaoNick;
	private Long stationId;
	private String partnerType;
	private Date birthday;
	private String lng;
	private String lat;
	private String villageDetail;
	private String villageCode;
	private String isOnTown;
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTaobaoUserId() {
		return taobaoUserId;
	}
	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
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
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getAddressDetail() {
		return addressDetail;
	}
	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}
	public Date getGmtServiceBegin() {
		return gmtServiceBegin;
	}
	public void setGmtServiceBegin(Date gmtServiceBegin) {
		this.gmtServiceBegin = gmtServiceBegin;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getFlowerName() {
		return flowerName;
	}
	public void setFlowerName(String flowerName) {
		this.flowerName = flowerName;
	}
	public String getIdenNum() {
		return idenNum;
	}
	public void setIdenNum(String idenNum) {
		this.idenNum = idenNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAliPay() {
		return aliPay;
	}
	public void setAliPay(String aliPay) {
		this.aliPay = aliPay;
	}
	public String getTaobaoNick() {
		return taobaoNick;
	}
	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public String getPartnerType() {
		return partnerType;
	}
	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
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
	public String getVillageDetail() {
		return villageDetail;
	}
	public void setVillageDetail(String villageDetail) {
		this.villageDetail = villageDetail;
	}
	public String getIsOnTown() {
		return isOnTown;
	}
	public void setIsOnTown(String isOnTown) {
		this.isOnTown = isOnTown;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
	public String getTownCode() {
		return townCode;
	}
	public void setTownCode(String townCode) {
		this.townCode = townCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getVillageCode() {
		return villageCode;
	}
	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}
}
