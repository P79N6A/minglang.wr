package com.taobao.cun.auge.cuncounty.dto;

import java.util.List;

/**
 * 县服务中心
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyDto {
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
     * 状态
     */
    private CuntaoCountyStateEnum state;
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
     * 运营时间
     */
    private String operateDate;

	/**
	 * 县点标签
	 */
	private List<CuntaoCountyTagEnum> countyTags;

	public List<CuntaoCountyTagEnum> getCountyTags() {
		return countyTags;
	}

	public void setCountyTags(List<CuntaoCountyTagEnum> countyTags) {
		this.countyTags = countyTags;
	}

	public String getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
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

	public CuntaoCountyStateEnum getState() {
		return state;
	}

	public void setState(String state) {
		this.state = CuntaoCountyStateEnum.valueof(state);
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

}