package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.LogisticsStationStateEnum;

public class LogisticsStationDto  extends OperatorDto {

	private static final long serialVersionUID = 7126293569030958747L;
	
    private Long id;

    private Long cainiaoStationId;


    private String name;


    private String contactName;


    private Long parentId;


    private Integer stationType;


    private Integer status;


    private String contactMobile;


    private String contactPhone;


    private Long taobaoUserId;


    private String feature;


    private String serviceCode;


    private String managers;


    private LogisticsStationStateEnum state;

    private String logisticsStationNum;
    
    private Address address;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCainiaoStationId() {
		return cainiaoStationId;
	}

	public void setCainiaoStationId(Long cainiaoStationId) {
		this.cainiaoStationId = cainiaoStationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getStationType() {
		return stationType;
	}

	public void setStationType(Integer stationType) {
		this.stationType = stationType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getManagers() {
		return managers;
	}

	public void setManagers(String managers) {
		this.managers = managers;
	}

	public String getLogisticsStationNum() {
		return logisticsStationNum;
	}

	public void setLogisticsStationNum(String logisticsStationNum) {
		this.logisticsStationNum = logisticsStationNum;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public LogisticsStationStateEnum getState() {
		return state;
	}

	public void setState(LogisticsStationStateEnum state) {
		this.state = state;
	}
    
}
