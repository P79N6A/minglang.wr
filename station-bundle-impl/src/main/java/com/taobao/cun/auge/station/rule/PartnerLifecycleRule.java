package com.taobao.cun.auge.station.rule;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;

public class PartnerLifecycleRule implements Serializable {

	private static final long serialVersionUID = 7346499100977904220L;
	/**
	 * 新模型实例状态
	 */
	private PartnerInstanceStateEnum state;
	/**
	 * 业务类型
	 */
	private PartnerLifecycleBusinessTypeEnum businessType;

	/**
	 * 入驻协议签署标签
	 */
	private PartnerLifecycleRuleItem settledProtocol;

	/**
	 * 保证金标签
	 */
	private PartnerLifecycleRuleItem bond;

	/**
	 * 退出协议标签
	 */
	private PartnerLifecycleRuleItem quitProtocol;

	/**
	 * 物流审批
	 */
	private PartnerLifecycleRuleItem logisticsApprove;

	/**
	 * 角色审批
	 */
	private PartnerLifecycleRuleItem roleApprove;

	/**
	 * 小二确认
	 */
	private PartnerLifecycleRuleItem confirm;

	/**
	 * 系统任务
	 */
	private PartnerLifecycleRuleItem system;
	
	/**
	 * 装修状态
	 */
	private PartnerLifecycleRuleItem decorateStatus;
	
	/**
	 * 培训状态
	 */
	private PartnerLifecycleRuleItem courseStatus;
	
	
	public PartnerLifecycleRuleItem getDecorateStatus() {
		return decorateStatus;
	}

	public void setDecorateStatus(PartnerLifecycleRuleItem decorateStatus) {
		this.decorateStatus = decorateStatus;
	}

	public PartnerLifecycleRuleItem getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(PartnerLifecycleRuleItem courseStatus) {
		this.courseStatus = courseStatus;
	}

	public PartnerInstanceStateEnum getState() {
		return state;
	}

	public void setState(PartnerInstanceStateEnum state) {
		this.state = state;
	}

	public PartnerLifecycleRuleItem getSettledProtocol() {
		return settledProtocol;
	}

	public void setSettledProtocol(PartnerLifecycleRuleItem settledProtocol) {
		this.settledProtocol = settledProtocol;
	}

	public PartnerLifecycleRuleItem getBond() {
		return bond;
	}

	public void setBond(PartnerLifecycleRuleItem bond) {
		this.bond = bond;
	}

	public PartnerLifecycleRuleItem getQuitProtocol() {
		return quitProtocol;
	}

	public void setQuitProtocol(PartnerLifecycleRuleItem quitProtocol) {
		this.quitProtocol = quitProtocol;
	}

	public PartnerLifecycleRuleItem getLogisticsApprove() {
		return logisticsApprove;
	}

	public void setLogisticsApprove(PartnerLifecycleRuleItem logisticsApprove) {
		this.logisticsApprove = logisticsApprove;
	}

	public PartnerLifecycleRuleItem getRoleApprove() {
		return roleApprove;
	}

	public void setRoleApprove(PartnerLifecycleRuleItem roleApprove) {
		this.roleApprove = roleApprove;
	}

	public PartnerLifecycleRuleItem getConfirm() {
		return confirm;
	}

	public void setConfirm(PartnerLifecycleRuleItem confirm) {
		this.confirm = confirm;
	}

	public PartnerLifecycleRuleItem getSystem() {
		return system;
	}

	public void setSystem(PartnerLifecycleRuleItem system) {
		this.system = system;
	}

	public PartnerLifecycleBusinessTypeEnum getBusinessType() {
		return businessType;
	}

	public void setBusinessType(PartnerLifecycleBusinessTypeEnum businessType) {
		this.businessType = businessType;
	}

}
