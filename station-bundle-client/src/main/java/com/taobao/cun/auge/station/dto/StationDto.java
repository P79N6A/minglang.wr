package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.station.enums.StationFixedTypeEnum;
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
    private StationStatusEnum state;

    /**
     * 申请组织
     */
    private Long applyOrg;

    /**
     * 村点编号
     */
    private String stationNum;


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
    private StationFixedTypeEnum fixedType;
    
    
    /**
     * 服务站相关附件
     */
    private List<AttachementDto> attachements;
    
    /**
     * 服务站地址相关信息
     */
    private StationAddressDto  stationAddressDto;

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

	public List<AttachementDto> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<AttachementDto> attachements) {
		this.attachements = attachements;
	}

	public StationAddressDto getStationAddressDto() {
		return stationAddressDto;
	}

	public void setStationAddressDto(StationAddressDto stationAddressDto) {
		this.stationAddressDto = stationAddressDto;
	}

	public StationFixedTypeEnum getFixedType() {
		return fixedType;
	}

	public void setFixedType(StationFixedTypeEnum fixedType) {
		this.fixedType = fixedType;
	}

	public StationStatusEnum getState() {
		return state;
	}

	public void setState(StationStatusEnum state) {
		this.state = state;
	}
}
