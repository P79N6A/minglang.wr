package com.taobao.cun.auge.cuncounty.dto.edit;

import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 办公场地信息
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyOfficeEditDto {
	@NotBlank(message="办公场地信息:办公地址不能为空")
    private String address;
	@NotNull(message="办公场地信息:办公面积不能为空")
    private Integer buildingArea;
	@NotNull(message="办公场地信息:租赁起始时间不能为空")
    private Date gmtRentStart;
	@NotNull(message="办公场地信息:租赁结束时间不能为空")
    private Date gmtRentEnd;
	@NotBlank(message="办公场地信息:租赁协议不能为空")
    private String attachments;
    @NotNull(message="办公场地信息:县服务中心不能为空")
    private Long countyId;
    @NotBlank(message="办公场地信息:操作人不能为空")
    private String operator;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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
		this.gmtRentStart = DateUtils.truncate(gmtRentStart, Calendar.DATE);
	}

	public Date getGmtRentEnd() {
		return gmtRentEnd;
	}

	public void setGmtRentEnd(Date gmtRentEnd) {
		this.gmtRentEnd = DateUtils.truncate(gmtRentEnd, Calendar.DATE);
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

}