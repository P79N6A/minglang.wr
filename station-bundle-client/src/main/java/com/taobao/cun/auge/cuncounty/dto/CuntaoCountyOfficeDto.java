package com.taobao.cun.auge.cuncounty.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Splitter;

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
    private Integer buildingArea;

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
    
    public List<String> getAttachmentArray(){
    	if(attachments != null) {
    		return new ArrayList<String>();
    	}
    	return Splitter.on(";").splitToList(attachments);
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

	public Integer getBuildingArea() {
		return buildingArea;
	}

	public void setBuildingArea(Integer buildingArea) {
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