package com.taobao.cun.auge.statemachine;

import java.util.stream.Stream;

import org.springframework.util.Assert;

/**
 * 状态机事件
 * @author zhenhuan.zhangzh
 *
 */
public enum StateMachineEvent {

	SETTLING_EVENT("SETTLING_EVENT","SETTLING"),
	DECORATING_EVENT("DECORATING_EVENT","DECORATING"),
	SERVICING_EVENT("SERVICING_EVENT","SERVICING"),
	CLOSING_EVENT("CLOSING_EVENT","CLOSING"),
	CLOSED_EVENT("CLOSED_EVENT","CLOSED"),
	QUITING_EVENT("QUITING_EVENT","QUITING"),
	QUIT_EVENT("QUIT_EVENT","QUIT");
	private String event;
	private String state;
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	private StateMachineEvent(String event,String state){
		this.event = event;
		this.state = state;
	}
	
	public static StateMachineEvent valueOfState(String state){
		StateMachineEvent sme = Stream.of(StateMachineEvent.values()).filter(value -> value.getState().equals(state)).findFirst().orElse(null);
		Assert.notNull(sme);
		return sme;
	}
	
	public static StateMachineEvent valueOfEvent(String event){
		StateMachineEvent sme =  Stream.of(StateMachineEvent.values()).filter(value -> value.getEvent().equals(event)).findFirst().orElse(null);
		Assert.notNull(sme);
		return sme;
	}
	
	
}
