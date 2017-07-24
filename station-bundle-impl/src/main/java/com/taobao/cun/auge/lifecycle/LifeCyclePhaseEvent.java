package com.taobao.cun.auge.lifecycle;

import com.taobao.cun.auge.station.dto.PartnerInstanceDto;

public class LifeCyclePhaseEvent {


	private String stateMachine;
	
	private String event;
	
	private String currentState;

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
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	private PartnerInstanceDto partnerInstanceDto;
	
	public LifeCyclePhaseEvent(String stateMachine, String event,PartnerInstanceDto partnerInstanceDto) {
		this.event = event;
		this.stateMachine = stateMachine;
		this.partnerInstanceDto = partnerInstanceDto;
	}
	
	public Object getPayload() {
		return partnerInstanceDto;
	}

}
