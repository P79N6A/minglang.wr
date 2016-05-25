package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.station.enums.StationStatusEnum;

public class StationDto extends OperatorDto implements Serializable{

	private static final long serialVersionUID = 5611747398839067505L;
	
	/**
     * 主键
     */
    private Long id;

    /**
     * 服务站名称
     */
    private String name;

    /**
     * 服务站简介
     */
    private String description;


    /**
     * 服务点状态
     */
    private String state;



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
     * 详细地址
     */
    private String address;

    /**
     * 申请组织
     */
    private Long applyOrg;

    /**
     * 村点编号
     */
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
     * 村/社区名称
详情
     */
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
    private String logisticsState;

    /**
     * 目前业态
     */
    private String format;

    /**
     * 固点，或者不固点
     */
    private String areaType;

    /**
     * 管理员user_id
     */
    private String managerId;

    /**
     * 服务商id
     */
    private Long providerId;

    /**
     * 其他特性，用于扩展服务站属性
     */
    private Map<String,String> feature;

    /**
     * 新的服务站状态
     */
    private StationStatusEnum status;

    /**
     * 场地固点类型 GOV_FIXED 政府固点
	 *	TRIPARTITE_FIXED 三方固点
     */
    private String fixedType;
    
    
    private List<AttachementDto> attachements;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getApplyOrg() {
		return applyOrg;
	}

	public void setApplyOrg(Long applyOrg) {
		this.applyOrg = applyOrg;
	}

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
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

	public String getCovered() {
		return covered;
	}

	public void setCovered(String covered) {
		this.covered = covered;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getLogisticsState() {
		return logisticsState;
	}

	public void setLogisticsState(String logisticsState) {
		this.logisticsState = logisticsState;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public Map<String, String> getFeature() {
		return feature;
	}

	public void setFeature(Map<String, String> feature) {
		this.feature = feature;
	}

	public StationStatusEnum getStatus() {
		return status;
	}

	public void setStatus(StationStatusEnum status) {
		this.status = status;
	}

	public String getFixedType() {
		return fixedType;
	}

	public void setFixedType(String fixedType) {
		this.fixedType = fixedType;
	}

	public List<AttachementDto> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<AttachementDto> attachements) {
		this.attachements = attachements;
	}
}
