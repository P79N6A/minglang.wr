package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class StationOpenSearchDto implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 服务站ID
	 */
	private Long id;
	
	/**
	 * 服务站名称
	 */
	private String name;
	
	/**
	 * 服务站用户的id
	 */
	private Long userId;
	
	/**
	 * 服务站申请人的名字
	 */
	private String userName;
	
	/**
	 * 服务站地址
	 */
	private String address;


	/**
	 * 服务站距离当前用户的距离
	 */
	private String distance;

	/**
	 * 合伙人角色
	 * @return
	 */
	private int userType;

	private String userTypeDesc;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getUserTypeDesc() {
		return userTypeDesc;
	}

	public void setUserTypeDesc(String userTypeDesc) {
		this.userTypeDesc = userTypeDesc;
	}
}
