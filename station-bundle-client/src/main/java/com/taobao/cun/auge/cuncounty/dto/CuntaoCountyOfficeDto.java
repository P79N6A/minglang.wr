package com.taobao.cun.auge.cuncounty.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.common.AttachmentVO;

/**
 * 办公场地信息
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyOfficeDto {
    private Long id;
    /**
     * 县服务中心ID
     */
    private Long countyId;

    /**
     * 办公地址
     */
    private String address;
    
    /**
     * 办公面积
     */
    private String buildingArea;

    /**
     * 租赁起始时间
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
     * AttachmentVO
     */
    private List<AttachmentVO> attachmentVOList;
    
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

	public String getRentStart() {
    	return formatDate(getGmtRentStart());
    }
    
    public String getRentEnd() {
    	return formatDate(getGmtRentEnd());
    }
    
    private String formatDate(Date date) {
    	if(date == null) {
    		return "";
    	}else {
    		return new SimpleDateFormat("yyyy-MM-dd").format(date);
    	}
    }
    public List<AttachmentVO> getAttachmentVOList() {
		return attachmentVOList;
	}

	public void setAttachmentVOList(List<AttachmentVO> attachmentVOList) {
		this.attachmentVOList = attachmentVOList;
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

	public String getBuildingArea() {
		return buildingArea;
	}

	public void setBuildingArea(String buildingArea) {
		this.buildingArea = buildingArea;
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

}