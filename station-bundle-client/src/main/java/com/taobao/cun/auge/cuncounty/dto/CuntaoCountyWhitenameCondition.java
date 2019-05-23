package com.taobao.cun.auge.cuncounty.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;

/**
 * 县白名单查询条件
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyWhitenameCondition implements Serializable {
	private static final long serialVersionUID = -6089130486736535695L;

	private String provinceCode;
	
	private String cityCode;
	
	private String countyCode;
	
	private String countyName;
	
	@NotNull(message="pageNum is null")
	private int pageNum = 1;
	
	@NotNull(message="pageSize is null")
	private int pageSize = 10;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getProvinceCode() {
		return StringUtils.defaultIfEmpty(provinceCode, null);
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return StringUtils.defaultIfEmpty(cityCode, null);
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCountyCode() {
		return StringUtils.defaultIfEmpty(countyCode, null);
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
}
