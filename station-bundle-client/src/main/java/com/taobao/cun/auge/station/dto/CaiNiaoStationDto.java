package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class CaiNiaoStationDto implements Serializable {

	private static final long serialVersionUID = 4477536541676182134L;

	public static final String USERTYPE_TP = "TP";// 合伙人

	public static final String USERTYPE_TPA = "TPA";// 淘帮手

	/**
	 * 站点id(菜鸟驿站)
	 */
	private long stationId;

	/**
	 * 父节点(菜鸟驿站)
	 */
	private long parentId;

	/**
	 * 站点名称
	 */
	private String stationName;

	/**
	 * 站点地址信息
	 */
	private AddressDto stationAddress;

	private String alipayAccount;
	private String contact;
	private String mobile;
	private String telephone;

	private String loginId;
	private Long taobaoUserId;
	/**
	 * 经度
	 */
	private String lat;

	/**
	 * 纬度
	 */
	private String lng;

	private String modifier;

	private Integer stationType;

	private Integer inventoryNum;

	private String village;
	private String villageDetail;

	/**
	 * 站点编号
	 */
	private String stationNum;

	/**
	 * 淘帮手对应合伙人的taobaouserId
	 */
	private Long tpTaobaoUserId;

	private String applierId;

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

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public AddressDto getStationAddress() {
		return stationAddress;
	}

	public void setStationAddress(AddressDto stationAddress) {
		this.stationAddress = stationAddress;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public void setStationType(Integer stationType) {
		this.stationType = stationType;
	}

	public Integer getStationType() {
		return stationType;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Integer getInventoryNum() {
		return inventoryNum;
	}

	public void setInventoryNum(Integer inventoryNum) {
		this.inventoryNum = inventoryNum;
	}

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public Long getTpTaobaoUserId() {
		return tpTaobaoUserId;
	}

	public void setTpTaobaoUserId(Long tpTaobaoUserId) {
		this.tpTaobaoUserId = tpTaobaoUserId;
	}

	public String getApplierId() {
		return applierId;
	}

	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}

}
