package com.taobao.cun.auge.lifecycle.tpa;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.lifecycle.validator.LifeCycleValidator;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSystemEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

@Component
@Phase(type="TPA",event=StateMachineEvent.SETTLING_EVENT,desc="淘帮手入驻中节点服务")
public class TPASettlingLifeCyclePhase extends AbstractLifeCyclePhase{

	
	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private LifeCycleValidator lifeCycleValidator;
	
	@Override
	@PhaseStepMeta
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		  PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		  lifeCycleValidator.validateSettling(partnerInstanceDto);
		  Long stationId = partnerInstanceDto.getStationId();
        if (stationId == null) {
            stationId = addStation(partnerInstanceDto);
        } else {
            StationDto stationDto = partnerInstanceDto.getStationDto();
            stationDto.setState(StationStateEnum.INVALID);
            stationDto.setStatus(StationStatusEnum.NEW);
            PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
            if (partnerDto != null) {
                stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
                stationDto.setAlipayAccount(partnerDto.getAlipayAccount());
                stationDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
            }
            updateStation(stationId, partnerInstanceDto);
        }
	}

	@Override
	@PhaseStepMeta
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		 PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		 addPartner(partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		addPartnerInstanceRel(partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		addPartnerLifeCycle(partnerInstanceDto);
	}

	private void addPartnerLifeCycle(PartnerInstanceDto partnerInstanceDto) {
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TPA);
		partnerLifecycleDto.copyOperatorDto(partnerInstanceDto);
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.SETTLING);
		partnerLifecycleDto.setPartnerInstanceId(partnerInstanceDto.getId());

		if (StringUtils.equals(OperatorTypeEnum.BUC.getCode(), partnerInstanceDto.getOperatorType().getCode())) {// 小二提交
			partnerLifecycleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNING);
			partnerLifecycleDto.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
			partnerLifecycleDto.setSystem(PartnerLifecycleSystemEnum.WAIT_PROCESS);
			partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		} else if (StringUtils.equals(OperatorTypeEnum.HAVANA.getCode(), partnerInstanceDto.getOperatorType().getCode())) {// 合伙人提交
			partnerLifecycleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNING);
			partnerLifecycleDto.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
			partnerLifecycleDto.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_AUDIT);
			partnerLifecycleDto.setSystem(PartnerLifecycleSystemEnum.WAIT_PROCESS);
			partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		}
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
	}

	@Override
	@PhaseStepMeta
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		//donothing
	}

	@Override
	@PhaseStepMeta
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		sendPartnerInstanceStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.START_SETTLING, partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta
	public void syncStationApply(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		syncStationApply(SyncStationApplyEnum.ADD, partnerInstanceDto.getId());
	}

	 
}
