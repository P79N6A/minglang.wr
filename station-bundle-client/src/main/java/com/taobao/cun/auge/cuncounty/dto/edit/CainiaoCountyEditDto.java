package com.taobao.cun.auge.cuncounty.dto.edit;

import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 菜鸟县仓
 * 
 * @author chengyu.zhoucy
 *
 */
public class CainiaoCountyEditDto {
	@NotNull(message="菜鸟县仓:县服务中心不能为空")
    private Long countyId;
	@NotBlank(message="菜鸟县仓:详细地址不能为空")
    private String address;
	@NotBlank(message="菜鸟县仓:仓储面积不能为空")
    private String storageArea;
	@NotNull(message="菜鸟县仓:租赁开始时间不能为空")
    private Date gmtRentStart;
	@NotNull(message="菜鸟县仓:租赁结束时间不能为空")
    private Date gmtRentEnd;
    @NotBlank(message="菜鸟县仓:租赁协议不能为空")
    private String attachments;
    @NotBlank(message="菜鸟县仓:省不能为空")
    private String provinceName;
    @NotBlank(message="菜鸟县仓:省行政CODE不能为空")
    private String provinceCode;
    private String countyName;
    private String countyCode;
    @NotBlank(message="菜鸟县仓:镇不能为空")
    private String townName;
    @NotBlank(message="菜鸟县仓:镇行政CODE不能为空")
    private String townCode;
    /**
     * 市名
     */
    private String cityName;
    /**
     * 市行政CODE
     */
    private String cityCode;
    @NotBlank(message="菜鸟县仓:操作人不能为空")
    private String operator;
    /**
     * 仓类型
     */
    private String storeType;

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
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

}