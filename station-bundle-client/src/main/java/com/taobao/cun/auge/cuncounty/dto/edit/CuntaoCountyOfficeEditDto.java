package com.taobao.cun.auge.cuncounty.dto.edit;

import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
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
	@NotBlank(message="办公场地:省不能为空")
    private String provinceName;
	@NotBlank(message="办公场地:省CODE不能为空")
    private String provinceCode;
    private String cityName;
    private String cityCode;
    private String countyName;
    private String countyCode;
    private String townName;
    private String townCode;
	@NotBlank(message="办公场地信息:办公地址不能为空")
    private String address;
	@NotBlank(message="办公场地信息:办公面积不能为空")
	@Digits(integer=Integer.MAX_VALUE,fraction=0,message="办公场地信息:办公面积必须为正整数")
	@Min(value=0,message="办公场地信息:办公面积必须为正整数")
    private String buildingArea;
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