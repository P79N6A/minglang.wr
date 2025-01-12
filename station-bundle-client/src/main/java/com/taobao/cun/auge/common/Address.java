package com.taobao.cun.auge.common;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class Address implements Serializable {

	private static final long serialVersionUID = -3987732618970128485L;

	private String province;

	private String city;

	private String county;

	private String town;

	private String village;

	private String provinceDetail;

	private String cityDetail;

	private String countyDetail;

	private String townDetail;

	private String villageDetail;

	private String addressDetail;

	private String lng;

	private String lat;

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

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getProvinceDetail() {
		return provinceDetail;
	}

	public void setProvinceDetail(String provinceDetail) {
		this.provinceDetail = provinceDetail;
	}

	public String getCityDetail() {
		return cityDetail;
	}

	public void setCityDetail(String cityDetail) {
		this.cityDetail = cityDetail;
	}

	public String getCountyDetail() {
		return countyDetail;
	}

	public void setCountyDetail(String countyDetail) {
		this.countyDetail = countyDetail;
	}

	public String getTownDetail() {
		return townDetail;
	}

	public void setTownDetail(String townDetail) {
		this.townDetail = townDetail;
	}

	public String getVillageDetail() {
		return villageDetail;
	}

	public void setVillageDetail(String villageDetail) {
		this.villageDetail = villageDetail;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
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
	
	public String buildAddressDetail() {
		StringBuilder sBuilder = new StringBuilder();

		if (StringUtils.isNotEmpty(provinceDetail)) {
			sBuilder.append(provinceDetail);
		}
		if (StringUtils.isNotEmpty(cityDetail)) {
			sBuilder.append(cityDetail);
		}
		if (StringUtils.isNotEmpty(countyDetail)) {
			sBuilder.append(countyDetail);
		}
		if (StringUtils.isNotEmpty(townDetail)) {
			sBuilder.append(townDetail);
		}

		if (StringUtils.isNotEmpty(villageDetail)) {
			sBuilder.append(villageDetail);
		}

		if (StringUtils.isNotEmpty(addressDetail)) {
			sBuilder.append(addressDetail);
		}
		return sBuilder.toString();
	}
}
