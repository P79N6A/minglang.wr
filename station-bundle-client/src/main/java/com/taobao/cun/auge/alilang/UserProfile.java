package com.taobao.cun.auge.alilang;

import java.io.Serializable;
import java.util.Date;

public class UserProfile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8034775442922893660L;

	private String stationName;
	
	private String userName;
	
	private int joinDays;
	
	private Long stationId;
	
	private Date birthday;
	
	private String flowerName;
	
	private Long taobaoUserId;

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getJoinDays() {
		return joinDays;
	}

	public void setJoinDays(int joinDays) {
		this.joinDays = joinDays;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getFlowerName() {
		return flowerName;
	}

	public void setFlowerName(String flowerName) {
		this.flowerName = flowerName;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}
	
	
}
