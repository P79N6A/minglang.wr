package com.taobao.cun.auge.lifecycle;

import java.util.Map;

import org.springframework.util.Assert;

import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;

/**
 * 
 * @author zhenhuan.zhangzh
 *
 */
public class LifeCyclePhaseEvent {


	private String stateMachine;
	
	private StateMachineEvent event;
	
	private String currentState;

	private Map<String,Object> extensionInfo;
	
	public Map<String, Object> getExtensionInfo() {
		return extensionInfo;
	}

	public void setExtensionInfo(Map<String, Object> extensionInfo) {
		this.extensionInfo = extensionInfo;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public String getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(String stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	public StateMachineEvent getEvent() {
		return event;
	}

	public void setEvent(StateMachineEvent event) {
		Assert.notNull(event);
		this.event = event;
	}
	
	private PartnerInstanceDto payload;
	
	public LifeCyclePhaseEvent(){
		
	}
	
	public LifeCyclePhaseEvent(String stateMachine, StateMachineEvent event,PartnerInstanceDto partnerInstanceDto) {
		Assert.notNull(stateMachine);
		Assert.notNull(event);
		Assert.notNull(partnerInstanceDto);
		this.event = event;
		this.stateMachine = stateMachine;
		this.payload = partnerInstanceDto;
	}
	
	public Object getPayload() {
		return payload;
	}

	public void setPayload(PartnerInstanceDto payload) {
		this.payload = payload;
	}

}
