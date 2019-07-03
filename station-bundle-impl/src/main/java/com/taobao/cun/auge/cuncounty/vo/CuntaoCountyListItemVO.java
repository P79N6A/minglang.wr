package com.taobao.cun.auge.cuncounty.vo;

import java.util.Date;

public class CuntaoCountyListItemVO {
	private Long id;
    /**
     * 县服务中心名
     */
    private String name;
    /**
     * 所属组织
     */
    private Long orgId;
    
    /**
     * 组织id path
     */
    private String fullIdPath;
    
    /**
     * 组织name path
     */
    private String fullNamePath;
    /**
     * 状态
     */
    private String state;
    /**
     * 省名
     */
    private String provinceName;
    /**
     * 市名
     */
    private String cityName;
    /**
     * 县名
     */
    private String countyName;
    /**
     * 镇名
     */
    private String townName;

    private String serialNum;
    
    private Date gmtProtocolStartDate;
    
    private Date gmtProtocolEndDate;
    /**
     * 运营时间
     */
    private Date operateDate;

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getTownName() {
		return townName;
	}
	public void setTownName(String townName) {
		this.townName = townName;
	}
	public Date getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
	public Date getGmtProtocolStartDate() {
		return gmtProtocolStartDate;
	}
	public void setGmtProtocolStartDate(Date gmtProtocolStartDate) {
		this.gmtProtocolStartDate = gmtProtocolStartDate;
	}
	public Date getGmtProtocolEndDate() {
		return gmtProtocolEndDate;
	}
	public void setGmtProtocolEndDate(Date gmtProtocolEndDate) {
		this.gmtProtocolEndDate = gmtProtocolEndDate;
	}
	public String getFullIdPath() {
		return fullIdPath;
	}
	public void setFullIdPath(String fullIdPath) {
		this.fullIdPath = fullIdPath;
	}
	public String getFullNamePath() {
		return fullNamePath;
	}
	public void setFullNamePath(String fullNamePath) {
		this.fullNamePath = fullNamePath;
	}
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
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
}
