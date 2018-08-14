package com.taobao.cun.auge.testuser;

import java.io.Serializable;

public class UserMatchInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6474981692307367033L;

	/**
	 * 大家电用户
	 */
	private boolean bigElecUser;
	
	/**
	 * 快消用户
	 */
	private boolean fmcgUser;
	
	/**
	 * 小家电用户
	 */
	private boolean smallElecUser;

	public boolean isBigElecUser() {
		return bigElecUser;
	}

	public void setBigElecUser(boolean bigElecUser) {
		this.bigElecUser = bigElecUser;
	}

	public boolean isFmcgUser() {
		return fmcgUser;
	}

	public void setFmcgUser(boolean fmcgUser) {
		this.fmcgUser = fmcgUser;
	}

	public boolean isSmallElecUser() {
		return smallElecUser;
	}

	public void setSmallElecUser(boolean smallElecUser) {
		this.smallElecUser = smallElecUser;
	}
}
