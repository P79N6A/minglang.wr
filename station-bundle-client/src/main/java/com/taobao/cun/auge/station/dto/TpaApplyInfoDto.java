package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;

import com.taobao.cun.attachment.dto.AttachmentDto;
import com.taobao.cun.auge.common.Address;

public class TpaApplyInfoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6688845704757025495L;

	private Long stationId;

	private Long partnerStationId;

	private Long taobaoUserId;

	private String stationNum;// 村服务点编号

	private String name;// 服务点名

	private Long partnerTaobaoUserId;// 合伙人淘宝UserId

	private String taobaoNick;// 淘宝Nick

	private String applierName; // 申请人姓名

	private String alipayAccount;// 支付宝账号

	private String email;// email

	private String mobile;// 手机号码

	private Address address;// 地址

	private String villageFlag;// 是否本村 Y,N

	private String placeFlag;// 有无经营场所

	private String category;// 行业类型

	private String covered;// 覆盖人口

	private String products;// 特色农产品

	private String logisticsState;// 物流情况

	private String desc;// 现状描述

	private String managementType;

	private List<AttachmentDto> attachements;

	private String[] categories = {"综合","农资","家电","3C数码","汽摩配","家装","服装","母婴","日百","餐饮","其他"};

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getPartnerStationId() {
		return partnerStationId;
	}

	public void setPartnerStationId(Long partnerStationId) {
		this.partnerStationId = partnerStationId;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPartnerTaobaoUserId() {
		return partnerTaobaoUserId;
	}

	public void setPartnerTaobaoUserId(Long partnerTaobaoUserId) {
		this.partnerTaobaoUserId = partnerTaobaoUserId;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getVillageFlag() {
		return villageFlag;
	}

	public void setVillageFlag(String villageFlag) {
		this.villageFlag = villageFlag;
	}

	public String getPlaceFlag() {
		return placeFlag;
	}

	public void setPlaceFlag(String placeFlag) {
		this.placeFlag = placeFlag;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getManagementType() {
		return managementType;
	}

	public void setManagementType(String managementType) {
		this.managementType = managementType;
	}

	public List<AttachmentDto> getAttachements() {
		return attachements;
	}

	public void setAttachements(List<AttachmentDto> attachements) {
		this.attachements = attachements;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}
}
