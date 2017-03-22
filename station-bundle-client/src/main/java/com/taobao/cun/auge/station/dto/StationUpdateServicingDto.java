package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.StationAreaTypeEnum;
import com.taobao.cun.auge.station.enums.StationFixedTypeEnum;
import com.taobao.cun.auge.station.enums.StationlLogisticsStateEnum;

public class StationUpdateServicingDto extends OperatorDto implements Serializable{

	private static final long serialVersionUID = 5611747398839067505L;
	
	/**
	 * 站点id
	 */
	private Long stationId;

    /**
     * 服务站名称
     */
    private String name;

    /**
     * 服务站简介
     */
    private String description;


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
    private StationlLogisticsStateEnum logisticsState;

    /**
     * 目前业态
     */
    private String format;

    /**
     * 固点，或者不固点
     */
    private StationAreaTypeEnum areaType;

    /**
     * 管理员user_id
     */
    private String managerId;

    /**
     * 其他特性，用于扩展服务站属性
     */
    private Map<String,String> feature;

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
    private Address  address;
    
    private PartnerProtocolRelDto fixedProtocols;
    
	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
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

	public StationlLogisticsStateEnum getLogisticsState() {
		return logisticsState;
	}

	public void setLogisticsState(StationlLogisticsStateEnum logisticsState) {
		this.logisticsState = logisticsState;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public Map<String, String> getFeature() {
		return feature;
	}

	public void setFeature(Map<String, String> feature) {
		this.feature = feature;
	}

	public List<AttachementDto> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<AttachementDto> attachements) {
		this.attachements = attachements;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public StationFixedTypeEnum getFixedType() {
		return fixedType;
	}

	public void setFixedType(StationFixedTypeEnum fixedType) {
		this.fixedType = fixedType;
	}

	public StationAreaTypeEnum getAreaType() {
		return areaType;
	}

	public void setAreaType(StationAreaTypeEnum areaType) {
		this.areaType = areaType;
	}

	public PartnerProtocolRelDto getFixedProtocols() {
		return fixedProtocols;
	}

	public void setFixedProtocols(PartnerProtocolRelDto fixedProtocols) {
		this.fixedProtocols = fixedProtocols;
	}	
}