package com.taobao.cun.auge.cuncounty.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordSummaryDto;
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
     * 组织id path
     */
    private String fullIdPath;
    
    /**
     * 组织name path
     */
    private String fullNamePath;
    /**
     * 状态
     */
    private CuntaoCountyStateEnum state;
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
     * 镇名
     */
    private String townName;
    
    /**
     * office详细地址
     */
    private String officeAddress;
    
    /**
     * 运营时间
     */
    private String operateDate;

	/**
	 * 政府拜访记录
	 */
	private CuntaoGovContactRecordSummaryDto cuntaoGovContactRecordSummaryDto;
    
    /**
     * 县小二
     */
    private List<CuntaoUserOrgVO> countyLeaders = Lists.newArrayList();
    
    /**
     * 区域经理
     */
    private List<CuntaoUserOrgVO> areaLeaders = Lists.newArrayList();
    
    /**
     * 省负责人
     */
    private List<CuntaoUserOrgVO> provinceLeaders = Lists.newArrayList();

	/**
	 * 协议编号
	 */
	private String serialNum;
    /**
     * 协议开始时间
     */
    private String protocolStartDate;
    
    /**
     * 协议结束时间
     */
    private String protocolEndDate;

	/**
	 * 协议即将失效
	 */
	private boolean protocolWillExpire = false;

	public boolean isProtocolWillExpire() {
		return protocolWillExpire;
	}

	public void setProtocolWillExpire(boolean protocolWillExpire) {
		this.protocolWillExpire = protocolWillExpire;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public String getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}

	public String getFullIdPath() {
		return fullIdPath;
	}

	public void setFullIdPath(String fullIdPath) {
		this.fullIdPath = fullIdPath;
	}

	public String getFullNamePath() {
		return fullNamePath;
	}

	public void setFullNamePath(String fullNamePath) {
		this.fullNamePath = fullNamePath;
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

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public List<CuntaoUserOrgVO> getCountyLeaders() {
		return countyLeaders == null ? new ArrayList<CuntaoUserOrgVO>() : countyLeaders;
	}

	public void setCountyLeaders(List<CuntaoUserOrgVO> countyLeaders) {
		this.countyLeaders = countyLeaders;
	}

	public List<CuntaoUserOrgVO> getAreaLeaders() {
		return areaLeaders == null ? new ArrayList<CuntaoUserOrgVO>() : areaLeaders;
	}

	public void setAreaLeaders(List<CuntaoUserOrgVO> areaLeaders) {
		this.areaLeaders = areaLeaders;
	}

	public List<CuntaoUserOrgVO> getProvinceLeaders() {
		return provinceLeaders == null ? new ArrayList<CuntaoUserOrgVO>() : provinceLeaders;
	}

	public void setProvinceLeaders(List<CuntaoUserOrgVO> provinceLeaders) {
		this.provinceLeaders = provinceLeaders;
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

	public CuntaoGovContactRecordSummaryDto getCuntaoGovContactRecordSummaryDto() {
		return cuntaoGovContactRecordSummaryDto;
	}

	public void setCuntaoGovContactRecordSummaryDto(CuntaoGovContactRecordSummaryDto cuntaoGovContactRecordSummaryDto) {
		this.cuntaoGovContactRecordSummaryDto = cuntaoGovContactRecordSummaryDto;
	}
}
