package com.taobao.cun.auge.logistics.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.Address;

public class LogisticsStationQueryDto implements Serializable{

	private static final long serialVersionUID = -5327156834251636500L;

	// 站点地址
	private Address address;

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
	private String manager;

	// 站点类型，县物流站点= 3，村物流站点=4
	private Integer stationType;
	
	//private Long 

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public Integer getStationType() {
		return stationType;
	}

	public void setStationType(Integer stationType) {
		this.stationType = stationType;
	}

}
