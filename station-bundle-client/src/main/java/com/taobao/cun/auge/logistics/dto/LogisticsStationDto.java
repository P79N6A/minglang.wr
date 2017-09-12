package com.taobao.cun.auge.logistics.dto;

import java.util.Map;
import java.util.Set;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.logistics.enums.LogisticsStationStateEnum;

public class LogisticsStationDto  extends OperatorDto {

	private static final long serialVersionUID = 7126293569030958747L;
	
	//主键
	private Long id;
	
	// 物流地址
	private Address address;

	// 菜鸟站点id
	private Long cainiaoStationId;

	// 父菜鸟站点id
	private Long parentId;

	// 站点名称
	private String name;

	// 联系人淘宝账号id，村服务站是合伙人的淘宝id，县服务中心是没有的
	private Long taobaoUserId;

	// 联系人姓名，合伙人姓名，或者县小二的姓名
	private String contactName;

	// 联系人手机号码
	private String contactMobile;

	// 联系人电话号码
	private String contactPhone;

	// 站点管理员，合伙人淘宝nick，或者县服务中心小二淘宝账号
	private Set<String> managers;

	// 站点类型，县物流站点= 3，村物流站点=4
	private Integer stationType;

	// 线上= 1；线下= -1；审核不通过= -2；测试中= 2，村淘都是线下=-1
	private Integer status;

	// 站点服务code,村淘=108
	private String serviceCode;
	
	// 其他特性
	private Map<String, String> featureMap;

	// 物流站审批状态
	private LogisticsStationStateEnum state;

	//物流编号
	private String logisticsStationNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Long getCainiaoStationId() {
		return cainiaoStationId;
	}

	public void setCainiaoStationId(Long cainiaoStationId) {
		this.cainiaoStationId = cainiaoStationId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
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

	public Set<String> getManagers() {
		return managers;
	}

	public void setManagers(Set<String> managers) {
		this.managers = managers;
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

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Map<String, String> getFeatureMap() {
		return featureMap;
	}

	public void setFeatureMap(Map<String, String> featureMap) {
		this.featureMap = featureMap;
	}

	public LogisticsStationStateEnum getState() {
		return state;
	}

	public void setState(LogisticsStationStateEnum state) {
		this.state = state;
	}

	public String getLogisticsStationNum() {
		return logisticsStationNum;
	}

	public void setLogisticsStationNum(String logisticsStationNum) {
		this.logisticsStationNum = logisticsStationNum;
	}
}
