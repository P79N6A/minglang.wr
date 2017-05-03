package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.Address;

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
	private Address stationAddress;

	private String alipayAccount;
	private String contact;
	private String mobile;
	private String telephone;

	private String loginId;
	private Long taobaoUserId;


	private String modifier;

	private Integer stationType;

	private Integer inventoryNum;

	/**
	 * 站点编号
	 */
	private String stationNum;

	/**
	 * 淘帮手对应合伙人的taobaouserId
	 */
	private Long tpTaobaoUserId;

	private String applierId;
	
	private String isOnTown;


	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}


	public Address getStationAddress() {
		return stationAddress;
	}

	public void setStationAddress(Address stationAddress) {
		this.stationAddress = stationAddress;
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

	public String getIsOnTown() {
		return isOnTown;
	}

	public void setIsOnTown(String isOnTown) {
		this.isOnTown = isOnTown;
	}

}
