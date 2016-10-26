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

	// 父站点id
	private Long parentStationId;

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
	
	//合伙人级别
	private String partnerInstanceLevel;
	
	//是否是当前人
	private String isCurrent;

	// ======生命週期========
	// 业务类型
	private String businessType;

	// 当前步骤
	private String currentStep;

	// 入驻协议
	private String settledProtocol;

	// 入驻协议操作符
	private boolean settledProtocolOp;

	// 保证金
	private String bond;

	// 保证金操作符
	private boolean bondOp;

	// 角色审批
	private String roleApprove;

	// 角色审批操作符
	private boolean roleApproveOp;

	// 退出协议
	private String quitProtocol;

	// 退出协议操作符
	private boolean quitProtocolOp;

	// 物流审批
	private String logisticsApprove;

	// 物流审批操作符
	private boolean logisticsApproveOp;

	// 小二确认
	private String confirm;

	// 小二确认操作符
	private boolean confirmOp;

	// 系统操作
	private String system;

	// 系统操作操作符
	private boolean systemOp;
	
	// ======培训和课程========
	
	//培训状态
	private String courseStatus;
	
	//课程状态
	private String decorateStatus;
	
	
	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	public String getDecorateStatus() {
		return decorateStatus;
	}

	public void setDecorateStatus(String decorateStatus) {
		this.decorateStatus = decorateStatus;
	}

	public Long getParentStationId() {
		return parentStationId;
	}

	public void setParentStationId(Long parentStationId) {
		this.parentStationId = parentStationId;
	}

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

	public boolean isSettledProtocolOp() {
		return settledProtocolOp;
	}

	public void setSettledProtocolOp(boolean settledProtocolOp) {
		this.settledProtocolOp = settledProtocolOp;
	}

	public boolean isBondOp() {
		return bondOp;
	}

	public void setBondOp(boolean bondOp) {
		this.bondOp = bondOp;
	}

	public boolean isRoleApproveOp() {
		return roleApproveOp;
	}

	public void setRoleApproveOp(boolean roleApproveOp) {
		this.roleApproveOp = roleApproveOp;
	}

	public String getQuitProtocol() {
		return quitProtocol;
	}

	public void setQuitProtocol(String quitProtocol) {
		this.quitProtocol = quitProtocol;
	}

	public boolean isQuitProtocolOp() {
		return quitProtocolOp;
	}

	public void setQuitProtocolOp(boolean quitProtocolOp) {
		this.quitProtocolOp = quitProtocolOp;
	}

	public String getLogisticsApprove() {
		return logisticsApprove;
	}

	public void setLogisticsApprove(String logisticsApprove) {
		this.logisticsApprove = logisticsApprove;
	}

	public boolean isLogisticsApproveOp() {
		return logisticsApproveOp;
	}

	public void setLogisticsApproveOp(boolean logisticsApproveOp) {
		this.logisticsApproveOp = logisticsApproveOp;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public boolean isConfirmOp() {
		return confirmOp;
	}

	public void setConfirmOp(boolean confirmOp) {
		this.confirmOp = confirmOp;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public boolean isSystemOp() {
		return systemOp;
	}

	public void setSystemOp(boolean systemOp) {
		this.systemOp = systemOp;
	}

	public String getPartnerInstanceLevel() {
		return partnerInstanceLevel;
	}

	public void setPartnerInstanceLevel(String partnerInstanceLevel) {
		this.partnerInstanceLevel = partnerInstanceLevel;
	}

	public String getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
	}
}
