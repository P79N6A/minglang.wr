package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class AddressDto implements Serializable {

	private static final long serialVersionUID = 807106653230769143L;

	private String province;
	private String city;
	private String county;
	private String town;

	private String provinceDetail;
	private String cityDetail;
	private String countyDetail;
	private String townDetail;

	private String village;
	private String villageDetail;

	private String address;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getVillageDetail() {
		return villageDetail;
	}

	public void setVillageDetail(String villageDetail) {
		this.villageDetail = villageDetail;
	}

}
