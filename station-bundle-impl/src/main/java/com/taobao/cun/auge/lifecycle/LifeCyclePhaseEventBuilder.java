package com.taobao.cun.auge.lifecycle;

import java.util.Map;

import org.springframework.util.Assert;

import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;

public class LifeCyclePhaseEventBuilder {

	public  static LifeCyclePhaseEvent build(PartnerInstanceDto partnerInstanceDto,StateMachineEvent smEvent,Map<String,Object> extensionInfo){
		Assert.notNull(partnerInstanceDto);
		LifeCyclePhaseEvent event = new LifeCyclePhaseEvent();
		event.setStateMachine(partnerInstanceDto.getType().getCode()+"StateMachine");
		if(partnerInstanceDto.getState() !=null){
			event.setCurrentState(partnerInstanceDto.getState().getCode());
		}
		event.setEvent(smEvent);
		event.setPayload(partnerInstanceDto);
		if(extensionInfo !=null){
			event.setExtensionInfo(extensionInfo);
		}
		return event;
	}
	
	public  static LifeCyclePhaseEvent build(PartnerInstanceDto partnerInstanceDto,StateMachineEvent smEvent){
		return build(partnerInstanceDto,smEvent,null);
	}
}