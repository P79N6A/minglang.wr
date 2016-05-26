package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

/**
 * 服务站表 地址信息
 * @author quanzhu.wangqz
 *
 */
public class StationAddressDto implements Serializable {

	private static final long serialVersionUID = 4751209172429017101L;

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
    private String provinceDetail;

    /**
     * 市详细
     */
    private String cityDetail;

    /**
     * 县/区详细
     */
    private String countyDetail;

    /**
     * 乡镇详细
     */
    private String townDetail;
    
    /**
     * 村/社区编号
     */
    private String village;

    /**
     * 村/社区名称
详情
     */
    private String villageDetail;

    /**
     * 详细地址
     */
    private String address;
    
    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
}
