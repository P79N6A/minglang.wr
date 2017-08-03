package com.taobao.cun.auge.county.dto;

import java.io.Serializable;
import java.util.List;

public class CountyStationQueryCondition implements Serializable{

	private static final long serialVersionUID = 1L;

	// 大区Id
	private Long parentId;

	// 县服务中心名称
	private String name;
    private String countyCode;
    private String cityCode;
    private String provinceCode;

	// 排序
	private CountyStationOrderByEnum orderByEnum;

	private int pageStart;
	private int pageSize;

	// 县点状态数组
	private List<String> statusArray;
	
	private boolean isMobile;
	
	

	public boolean isMobile() {
		return isMobile;
	}

	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CountyStationOrderByEnum getOrderByEnum() {
		return orderByEnum;
	}

	public void setOrderByEnum(CountyStationOrderByEnum orderByEnum) {
		this.orderByEnum = orderByEnum;
	}

	public int getPageStart() {
		return pageStart;
	}

	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<String> getStatusArray() {
		return statusArray;
	}

	public void setStatusArray(List<String> statusArray) {
		this.statusArray = statusArray;
	}

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
