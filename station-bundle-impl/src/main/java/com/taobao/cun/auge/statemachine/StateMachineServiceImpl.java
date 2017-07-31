package com.taobao.cun.auge.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.shared.xfsm.core.XFSMEvent;
import com.alibaba.shared.xfsm.core.context.RequestContext;
import com.alibaba.shared.xfsm.support.spring.engine.XFSMEngine;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("stateMachineService")
@HSFProvider(serviceInterface = StateMachineService.class)
public class StateMachineServiceImpl implements StateMachineService {

	@Autowired
	private  XFSMEngine xfsm;
	
	@Override
	public void executePhase(LifeCyclePhaseEvent phaseEvent) {
		  RequestContext ctx = createRequestContext(phaseEvent);
	        try {
				xfsm.go(ctx);
			} catch (Exception e) {
				throw new AugeSystemException("executeLifeCyclePhase error!",e);
			}
	}

	private RequestContext createRequestContext(LifeCyclePhaseEvent phaseEvent) {
		 RequestContext ctx = new RequestContext(phaseEvent.getStateMachine());
		 if(phaseEvent.getCurrentState()!=null){
			ctx.setCurrentState(phaseEvent.getCurrentState());
		  }
		  ctx.set("event", phaseEvent.getEvent().getEvent());
		  ctx.set("payload", phaseEvent.getPayload());
	      ctx.events(new XFSMEvent(phaseEvent.getEvent().getEvent(),phaseEvent.getPayload()));
		return ctx;
	}

}
