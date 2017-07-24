package com.taobao.cun.auge.statemachine.component;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.shared.xfsm.core.annotation.Inject;
import com.alibaba.shared.xfsm.core.context.RequestContext;
import com.taobao.cun.auge.lifecycle.LifeCycleManager;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.statemachine.StateMachineComponent;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;

@StateMachineComponent(stateMachine={"TpStateMachine","TpaStateMachine"},actionKey="stateMachineComponent")
public class StateMachineComponentImpl {

	@Autowired
	private LifeCycleManager lifeCycleManager;
	
	private LifeCyclePhaseContext createContext(PartnerInstanceDto partnerInstanceDto, RequestContext ctx) {
		LifeCyclePhaseContext context = new LifeCyclePhaseContext();
		context.setPartnerInstance(partnerInstanceDto);
		context.setEvent(ctx.get("event"));;
		context.setUserType(partnerInstanceDto.getType().getCode());
		return context;
	}
	
	public void settling(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx){
		LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
		lifeCycleManager.settling(context);
	}

	
	
	public void decorating(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx){
		LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
		lifeCycleManager.decorating(context);
	}
	
	
	public void servicing(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx){
		LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
		lifeCycleManager.servicing(context);
	}
	
	
	public void closing(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx){
		LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
		lifeCycleManager.closing(context);
	}
	
	
	public void closed(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx){
		LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
		lifeCycleManager.closed(context);
	}
	
	public void quiting(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx){
		LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
		lifeCycleManager.quiting(context);
	}
	
	public void quit(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx){
		LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
		lifeCycleManager.quit(context);
	}
}
