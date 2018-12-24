package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
/**
 * 资产盘点，村点信息
 * @author quanzhu.wangqz
 *
 */
public class AssetCheckStationExtInfo  implements Serializable{
	
	private static final long serialVersionUID = -5728844728615991596L;
	/**
	 * 村点名称
	 */
	private String stationName;
	/**
	 * 村点编号
	 */
	private String stationNum;
	/**
	 * 村点地址
	 */
	private String address;
	/**
	 * 村点状态描述
	 */
	private String stateDesc;
	/**
	 * 村小二电话
	 */
	private String phone;
	/**
	 * 村小二名字
	 */
	private String partnerName;
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationNum() {
		return stationNum;
	}
	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStateDesc() {
		return stateDesc;
	}
	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
}
