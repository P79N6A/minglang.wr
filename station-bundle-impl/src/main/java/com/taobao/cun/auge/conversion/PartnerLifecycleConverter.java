package com.taobao.cun.auge.conversion;

import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.station.condition.PartnerLifecycleCondition;

public  class PartnerLifecycleConverter {

	public static PartnerLifecycleItems  convertToDomain(PartnerLifecycleCondition life) {
		PartnerLifecycleItems items = new PartnerLifecycleItems();
		if (life.getLifecycleId() != null) {
			items.setId(life.getLifecycleId());
		}
		if (life.getBond() != null) {
			items.setBond(life.getBond().getCode());
		}
		if (life.getBusinessType() != null) {
			items.setBusinessType(life.getBusinessType().getCode());
		}
		if (life.getConfirm() != null) {
			items.setConfirm(life.getConfirm().getCode());
		}
		if (life.getCurrentStep() != null) {
			items.setCurrentStep(life.getCurrentStep().getCode());
		}
		if (life.getLogisticsApprove() != null) {
			items.setLogisticsApprove(life.getLogisticsApprove().getCode());
		}
		if (life.getPartnerInstanceId() != null) {
			items.setPartnerInstanceId(life.getPartnerInstanceId());
		}
		if (life.getPartnerType() != null) {
			items.setPartnerType(life.getPartnerType().getCode());
		}
		if(life.getQuitProtocol() != null) {
			items.setQuitProtocol(life.getQuitProtocol().getCode());
		}
		if(life.getRoleApprove() != null) {
			items.setRoleApprove(life.getRoleApprove().getCode());
		}
		if(life.getSettledProtocol() != null) {
			items.setSettledProtocol(life.getSettledProtocol().getCode());
		}
		return items;
	}
}
