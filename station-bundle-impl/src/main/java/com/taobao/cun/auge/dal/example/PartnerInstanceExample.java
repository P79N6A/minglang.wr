package com.taobao.cun.auge.dal.example;

import java.io.Serializable;

public class PartnerInstanceExample implements Serializable {

	private static final long serialVersionUID = 1232528817432543217L;

	/**
	 * 村点所属组织id path
	 */
	private String orgIdPath;

	// 村点编号
	private String stationNum;

	// 村点名称
	private String stationName;

	// 村点管理员 小二工号或TP商淘宝userid
	private String managerId;

	// 服务商id
	private Long providerId;

	// 省
	private String province;

	// 市
	private String city;

	// 县
	private String county;

	// 镇
	private String town;

	// 人的姓名
	private String partnerName;

	// 淘宝nick
	private String taobaoNick;

	// 合伙人类型
	private String partnerType;

	// 合伙人状态
	private String partnerState;

	// 排序
	private String orderByClause;
	
	//======生命週期========
	//业务类型
	private String businessType;
	
	//入驻协议
	private String settledProtocol;
	
	//保证金
	private String bond;
	
	//当前步骤
	private String currentStep;
	
	//角色审批
	private String roleApprove;
	
	public String getOrgIdPath() {
		return orgIdPath;
	}

	public void setOrgIdPath(String orgIdPath) {
		this.orgIdPath = orgIdPath;
	}

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getPartnerState() {
		return partnerState;
	}

	public void setPartnerState(String partnerState) {
		this.partnerState = partnerState;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getSettledProtocol() {
		return settledProtocol;
	}

	public void setSettledProtocol(String settledProtocol) {
		this.settledProtocol = settledProtocol;
	}

	public String getBond() {
		return bond;
	}

	public void setBond(String bond) {
		this.bond = bond;
	}

	public String getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}

	public String getRoleApprove() {
		return roleApprove;
	}

	public void setRoleApprove(String roleApprove) {
		this.roleApprove = roleApprove;
	}
}
