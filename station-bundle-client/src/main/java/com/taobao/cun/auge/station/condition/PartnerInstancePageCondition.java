package com.taobao.cun.auge.station.condition;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleLogisticsApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;

public class PartnerInstancePageCondition extends PageQuery {

	private static final long serialVersionUID = 8658912725579809791L;

	// 村服务站所属村淘组织
	private String orgIdPath;

	// 村服务站编号
	private String stationNum;

	// 村服务站名称
	private String stationName;

	// 村服务站管理员
	private String managerId;

	// 村服务站地址
	private Address address;

	// 合伙人taobao昵称
	private String taobaoNick;

	// 合伙人姓名
	private String partnerName;

	// 合伙人状态
	private PartnerInstanceStateEnum partnerInstanceState;

	// 合伙人类型
	@NotNull(message = "partnerType is null")
	private PartnerInstanceTypeEnum partnerType;

	// 所属TP商id
	private Long providerId;

	// 生命周期业务类型
	private PartnerLifecycleBusinessTypeEnum businessType;

	// 保证金
	private PartnerLifecycleBondEnum bond;

	// 物流审批
	private PartnerLifecycleLogisticsApproveEnum logisticsApprove;

	// 退出协议
	private PartnerLifecycleQuitProtocolEnum quitProtocol;

	// 角色审批
	private PartnerLifecycleRoleApproveEnum roleApprove;

	// 入驻审批
	private PartnerLifecycleSettledApproveEnum settleApprove;

	// 入驻协议
	private PartnerLifecycleSettledProtocolEnum settleProtocol;

	// 当前阶段
	private PartnerLifecycleCurrentStepEnum currentStep;

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

	public PartnerInstanceStateEnum getPartnerInstanceState() {
		return partnerInstanceState;
	}

	public void setPartnerInstanceState(PartnerInstanceStateEnum partnerInstanceState) {
		this.partnerInstanceState = partnerInstanceState;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public PartnerLifecycleBondEnum getBond() {
		return bond;
	}

	public void setBond(PartnerLifecycleBondEnum bond) {
		this.bond = bond;
	}

	public PartnerLifecycleLogisticsApproveEnum getLogisticsApprove() {
		return logisticsApprove;
	}

	public void setLogisticsApprove(PartnerLifecycleLogisticsApproveEnum logisticsApprove) {
		this.logisticsApprove = logisticsApprove;
	}

	public PartnerLifecycleQuitProtocolEnum getQuitProtocol() {
		return quitProtocol;
	}

	public void setQuitProtocol(PartnerLifecycleQuitProtocolEnum quitProtocol) {
		this.quitProtocol = quitProtocol;
	}

	public PartnerLifecycleRoleApproveEnum getRoleApprove() {
		return roleApprove;
	}

	public void setRoleApprove(PartnerLifecycleRoleApproveEnum roleApprove) {
		this.roleApprove = roleApprove;
	}

	public PartnerLifecycleSettledApproveEnum getSettleApprove() {
		return settleApprove;
	}

	public void setSettleApprove(PartnerLifecycleSettledApproveEnum settleApprove) {
		this.settleApprove = settleApprove;
	}

	public PartnerLifecycleSettledProtocolEnum getSettleProtocol() {
		return settleProtocol;
	}

	public void setSettleProtocol(PartnerLifecycleSettledProtocolEnum settleProtocol) {
		this.settleProtocol = settleProtocol;
	}

	public PartnerLifecycleCurrentStepEnum getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(PartnerLifecycleCurrentStepEnum currentStep) {
		this.currentStep = currentStep;
	}

	public PartnerLifecycleBusinessTypeEnum getBusinessType() {
		return businessType;
	}

	public void setBusinessType(PartnerLifecycleBusinessTypeEnum businessType) {
		this.businessType = businessType;
	}

	public PartnerInstanceTypeEnum getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(PartnerInstanceTypeEnum partnerType) {
		this.partnerType = partnerType;
	}
}
