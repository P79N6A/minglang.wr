package com.taobao.cun.auge.cuncounty.dto;

import java.util.List;

import com.taobao.cun.auge.user.dto.CuntaoUserOrgVO;

/**
 * 
 * 列表查询时条目详情
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyListItem {
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
    private CuntaoCountyStateEnum cuntaoCountyStateEnum;
    /**
     * 省名
     */
    private String provinceName;
    /**
     * 市名
     */
    private String cityName;
    /**
     * 县名
     */
    private String countyName;
    
    /**
     * office详细地址
     */
    private String address;
    
    /**
     * 县小二
     */
    private List<CuntaoUserOrgVO> countyLeaders;
    
    /**
     * 区域经理
     */
    private List<CuntaoUserOrgVO> areaLeaders;
    
    /**
     * 省负责人
     */
    private List<CuntaoUserOrgVO> provinceLeaders;
    
    /**
     * 组织
     */
    private String orgFullNamePath;
    
    /**
     * 协议开始时间
     */
    private String protocolStartDate;
    
    /**
     * 协议结束时间
     */
    private String protocolEndDate;

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

	public CuntaoCountyStateEnum getCuntaoCountyStateEnum() {
		return cuntaoCountyStateEnum;
	}

	public void setCuntaoCountyStateEnum(CuntaoCountyStateEnum cuntaoCountyStateEnum) {
		this.cuntaoCountyStateEnum = cuntaoCountyStateEnum;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<CuntaoUserOrgVO> getCountyLeaders() {
		return countyLeaders;
	}

	public void setCountyLeaders(List<CuntaoUserOrgVO> countyLeaders) {
		this.countyLeaders = countyLeaders;
	}

	public List<CuntaoUserOrgVO> getAreaLeaders() {
		return areaLeaders;
	}

	public void setAreaLeaders(List<CuntaoUserOrgVO> areaLeaders) {
		this.areaLeaders = areaLeaders;
	}

	public List<CuntaoUserOrgVO> getProvinceLeaders() {
		return provinceLeaders;
	}

	public void setProvinceLeaders(List<CuntaoUserOrgVO> provinceLeaders) {
		this.provinceLeaders = provinceLeaders;
	}

	public String getOrgFullNamePath() {
		return orgFullNamePath;
	}

	public void setOrgFullNamePath(String orgFullNamePath) {
		this.orgFullNamePath = orgFullNamePath;
	}

	public String getProtocolStartDate() {
		return protocolStartDate;
	}

	public void setProtocolStartDate(String protocolStartDate) {
		this.protocolStartDate = protocolStartDate;
	}

	public String getProtocolEndDate() {
		return protocolEndDate;
	}

	public void setProtocolEndDate(String protocolEndDate) {
		this.protocolEndDate = protocolEndDate;
	}
}
