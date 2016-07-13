package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCourseStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleLogisticsApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSystemEnum;
/**
 * 合伙人生命周期 dto
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleDto  extends OperatorDto implements Serializable {

	private static final long serialVersionUID = -9216881285970436173L;
	
	/**
	 * 主键id
	 */
	private Long LifecycleId;
	/**
     * 合伙人类型
     */
    private PartnerInstanceTypeEnum partnerType;

    /**
     * 业务类型
     */
    private PartnerLifecycleBusinessTypeEnum businessType;

    /**
     * 入驻协议签署标签
     */
    private PartnerLifecycleSettledProtocolEnum settledProtocol;

    /**
     * 保证金标签
     */
    private PartnerLifecycleBondEnum bond;

    /**
     * 退出协议标签
     */
    private PartnerLifecycleQuitProtocolEnum quitProtocol;

    /**
     * 物流审批
     */
    private PartnerLifecycleLogisticsApproveEnum logisticsApprove;

    /**
     * 合伙人实例的主键
     */
    private Long partnerInstanceId;


    /**
     * 同一业务类型中，当前操作步骤
     */
    private PartnerLifecycleCurrentStepEnum currentStep;

    /**
     * 角色审批
     */
    private PartnerLifecycleRoleApproveEnum roleApprove;

    /**
     * 小二确认
     */
    private PartnerLifecycleConfirmEnum confirm;
    
    
    /**
     * 系统任务
     */
    private PartnerLifecycleSystemEnum system;
    
    /**
     * 装修状态
     */
    private PartnerLifecycleDecorateStatusEnum decorateStatus;
    
    /**
     * 培训状态
     */
    private PartnerLifecycleCourseStatusEnum courseStatus;
    

	public PartnerInstanceTypeEnum getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(PartnerInstanceTypeEnum partnerType) {
		this.partnerType = partnerType;
	}

	public PartnerLifecycleBusinessTypeEnum getBusinessType() {
		return businessType;
	}

	public void setBusinessType(PartnerLifecycleBusinessTypeEnum businessType) {
		this.businessType = businessType;
	}

	public PartnerLifecycleSettledProtocolEnum getSettledProtocol() {
		return settledProtocol;
	}

	public void setSettledProtocol(
			PartnerLifecycleSettledProtocolEnum settledProtocol) {
		this.settledProtocol = settledProtocol;
	}

	public PartnerLifecycleBondEnum getBond() {
		return bond;
	}

	public void setBond(PartnerLifecycleBondEnum bond) {
		this.bond = bond;
	}

	public PartnerLifecycleQuitProtocolEnum getQuitProtocol() {
		return quitProtocol;
	}

	public void setQuitProtocol(PartnerLifecycleQuitProtocolEnum quitProtocol) {
		this.quitProtocol = quitProtocol;
	}

	public PartnerLifecycleLogisticsApproveEnum getLogisticsApprove() {
		return logisticsApprove;
	}

	public void setLogisticsApprove(
			PartnerLifecycleLogisticsApproveEnum logisticsApprove) {
		this.logisticsApprove = logisticsApprove;
	}

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public PartnerLifecycleCurrentStepEnum getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(PartnerLifecycleCurrentStepEnum currentStep) {
		this.currentStep = currentStep;
	}

	public PartnerLifecycleRoleApproveEnum getRoleApprove() {
		return roleApprove;
	}

	public void setRoleApprove(PartnerLifecycleRoleApproveEnum roleApprove) {
		this.roleApprove = roleApprove;
	}

	public PartnerLifecycleConfirmEnum getConfirm() {
		return confirm;
	}

	public void setConfirm(PartnerLifecycleConfirmEnum confirm) {
		this.confirm = confirm;
	}

	public Long getLifecycleId() {
		return LifecycleId;
	}

	public void setLifecycleId(Long lifecycleId) {
		LifecycleId = lifecycleId;
	}

	public PartnerLifecycleSystemEnum getSystem() {
		return system;
	}

	public void setSystem(PartnerLifecycleSystemEnum system) {
		this.system = system;
	}

	public PartnerLifecycleDecorateStatusEnum getDecorateStatus() {
		return decorateStatus;
	}

	public void setDecorateStatus(PartnerLifecycleDecorateStatusEnum decorateStatus) {
		this.decorateStatus = decorateStatus;
	}

	public PartnerLifecycleCourseStatusEnum getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(PartnerLifecycleCourseStatusEnum courseStatus) {
		this.courseStatus = courseStatus;
	}

}

   