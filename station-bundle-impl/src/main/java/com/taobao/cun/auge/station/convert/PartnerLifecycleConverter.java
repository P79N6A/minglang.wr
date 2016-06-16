package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleLogisticsApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;

/**
 * 生命周期表转换
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleConverter {

	public static PartnerLifecycleItems toPartnerLifecycleItems(PartnerLifecycleDto partnerLifecycleDto) {
		PartnerLifecycleItems items = new PartnerLifecycleItems();
		if (partnerLifecycleDto.getLifecycleId() != null) {
			items.setId(partnerLifecycleDto.getLifecycleId());
		}
		if (partnerLifecycleDto.getBond() != null) {
			items.setBond(partnerLifecycleDto.getBond().getCode());
		}
		if (partnerLifecycleDto.getBusinessType() != null) {
			items.setBusinessType(partnerLifecycleDto.getBusinessType().getCode());
		}
		if (partnerLifecycleDto.getConfirm() != null) {
			items.setConfirm(partnerLifecycleDto.getConfirm().getCode());
		}
		if (partnerLifecycleDto.getCurrentStep() != null) {
			items.setCurrentStep(partnerLifecycleDto.getCurrentStep().getCode());
		}
		if (partnerLifecycleDto.getLogisticsApprove() != null) {
			items.setLogisticsApprove(partnerLifecycleDto.getLogisticsApprove().getCode());
		}
		if (partnerLifecycleDto.getPartnerInstanceId() != null) {
			items.setPartnerInstanceId(partnerLifecycleDto.getPartnerInstanceId());
		}
		if (partnerLifecycleDto.getPartnerType() != null) {
			items.setPartnerType(partnerLifecycleDto.getPartnerType().getCode());
		}
		if (partnerLifecycleDto.getQuitProtocol() != null) {
			items.setQuitProtocol(partnerLifecycleDto.getQuitProtocol().getCode());
		}
		if (partnerLifecycleDto.getRoleApprove() != null) {
			items.setRoleApprove(partnerLifecycleDto.getRoleApprove().getCode());
		}
		if (partnerLifecycleDto.getSettledProtocol() != null) {
			items.setSettledProtocol(partnerLifecycleDto.getSettledProtocol().getCode());
		}
		return items;
	}
	
	
	public static PartnerLifecycleDto toPartnerLifecycleDto(PartnerLifecycleItems items){
		if (items == null) {
			return null;
		}
		
		PartnerLifecycleDto lifecleDto = new PartnerLifecycleDto();
		
		lifecleDto.setPartnerType(PartnerInstanceTypeEnum.valueof(items.getPartnerType()));
		lifecleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.valueof(items.getBusinessType()));
		lifecleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.valueof(items.getSettledProtocol()));
		lifecleDto.setBond(PartnerLifecycleBondEnum.valueof(items.getBond()));
		lifecleDto.setQuitProtocol(PartnerLifecycleQuitProtocolEnum.valueof(items.getQuitProtocol()));
		lifecleDto.setLogisticsApprove(PartnerLifecycleLogisticsApproveEnum.valueof(items.getLogisticsApprove()));
		lifecleDto.setPartnerInstanceId(items.getPartnerInstanceId());
		lifecleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.valueof(items.getCurrentStep()));
		lifecleDto.setRoleApprove(PartnerLifecycleRoleApproveEnum.valueof(items.getRoleApprove()));
		lifecleDto.setConfirm(PartnerLifecycleConfirmEnum.valueof(items.getConfirm()));
		
		return lifecleDto;
	}
}
