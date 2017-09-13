package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

public class ValidateThreeAssetDto  implements Serializable{
	
	private static final long serialVersionUID = 3480187791284855742L;
	/**
	 * 村点id
	 */
	public Long stationId;
	/**
	 * 淘宝账号
	 */
	public Long taobaoUserId;
	/**
	 * 电视机后缀
	 */
	private String tvPostfix;
	/**
	 * 主机后缀
	 */
	private String mainPostfix;
	
	/**
	 * 显示器后缀
	 */
	private String displayPostfix;

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getTvPostfix() {
		return tvPostfix;
	}

	public void setTvPostfix(String tvPostfix) {
		this.tvPostfix = tvPostfix;
	}

	public String getMainPostfix() {
		return mainPostfix;
	}

	public void setMainPostfix(String mainPostfix) {
		this.mainPostfix = mainPostfix;
	}

	public String getDisplayPostfix() {
		return displayPostfix;
	}

	public void setDisplayPostfix(String displayPostfix) {
		this.displayPostfix = displayPostfix;
	}
}
