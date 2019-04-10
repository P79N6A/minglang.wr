package com.taobao.cun.auge.cuncounty.dto;

import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;
import com.taobao.cun.auge.common.AttachmentVO;

/**
 * 菜鸟县仓
 * 
 * @author chengyu.zhoucy
 *
 */
public class CainiaoCountyDto {
    private Long id;
    /**
     * 县服务中心CODE
     */
    private Long countyId;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 仓储面积
     */
    private String storageArea;
    /**
     * 租赁开始时间
     */
    private Date gmtRentStart;
    /**
     * 租赁结束时间
     */
    private Date gmtRentEnd;
    /**
     * 租赁协议
     */
    private String attachments;

    /**
     * 省名
     */
    private String provinceName;
    /**
     * 省行政CODE
     */
    private String provinceCode;
    /**
     * 市名
     */
    private String cityName;
    /**
     * 市行政CODE
     */
    private String cityCode;
    /**
     * 县名
     */
    private String countyName;
    /**
     * 县行政CODE
     */
    private String countyCode;

    /**
     * 镇名
     */
    private String townName;

    /**
     * 镇行政CODE
     */
    private String townCode;
    
    /**
     * 状态
     */
    private String state;
    
    /**
     * 菜鸟县仓ID
     */
    private Long cainiaoCountyId;
    
    /**
     * AttachmentVO
     */
    private List<AttachmentVO> attachmentVOList;
    
    /**
     * 是否为政府仓，{@code y}-是，{@code n}-否
     */
    private String govStore;
    
    public boolean isGovSupplyStore() {
    	return "y".equals(govStore);
    }
    
    public String getGovStore() {
		return govStore;
	}

	public void setGovStore(String govStore) {
		this.govStore = govStore;
	}

	public List<AttachmentVO> getAttachmentVOList() {
		return attachmentVOList;
	}

	public void setAttachmentVOList(List<AttachmentVO> attachmentVOList) {
		this.attachmentVOList = attachmentVOList;
	}

	public Long getCainiaoCountyId() {
		return cainiaoCountyId;
	}

	public void setCainiaoCountyId(Long cainiaoCountyId) {
		this.cainiaoCountyId = cainiaoCountyId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStorageArea() {
		return storageArea;
	}

	public void setStorageArea(String storageArea) {
		this.storageArea = storageArea;
	}

	public Date getGmtRentStart() {
		return gmtRentStart;
	}

	public void setGmtRentStart(Date gmtRentStart) {
		this.gmtRentStart = gmtRentStart;
	}

	public Date getGmtRentEnd() {
		return gmtRentEnd;
	}

	public void setGmtRentEnd(Date gmtRentEnd) {
		this.gmtRentEnd = gmtRentEnd;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public String getTownCode() {
		return townCode;
	}

	public void setTownCode(String townCode) {
		this.townCode = townCode;
	}

	public String toAdressDetail() {
		return Strings.nullToEmpty(provinceName) + 
				Strings.nullToEmpty(cityName)+ 
				Strings.nullToEmpty(countyName)+ 
				Strings.nullToEmpty(townName) + 
				Strings.nullToEmpty(address);
	}
}