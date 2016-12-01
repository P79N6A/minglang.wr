package com.taobao.cun.auge.alilang;

import java.io.Serializable;

public class UserProfile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8034775442922893660L;

	private String stationName;
	
	private String userName;
	
	private int joinDays;

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
	
	
}