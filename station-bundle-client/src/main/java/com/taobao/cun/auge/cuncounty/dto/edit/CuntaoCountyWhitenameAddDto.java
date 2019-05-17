package com.taobao.cun.auge.cuncounty.dto.edit;

/**
 * 添加白名单
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyWhitenameAddDto {
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
