package com.taobao.cun.auge.statemachine;

import com.alibaba.metrics.MetricLevel;
import com.alibaba.metrics.MetricManager;
import com.alibaba.metrics.MetricName;
import com.alibaba.metrics.Timer;
import com.alibaba.shared.xfsm.core.XFSMEvent;
import com.alibaba.shared.xfsm.core.context.RequestContext;
import com.alibaba.shared.xfsm.support.spring.engine.XFSMEngine;

import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("stateMachineService")
@HSFProvider(serviceInterface = StateMachineService.class)
public class StateMachineServiceImpl implements StateMachineService {

	Timer timer = MetricManager.getTimer("stateMachine", MetricName.build("stateMachine.create").level(MetricLevel.CRITICAL));

	@Autowired
	private  XFSMEngine xfsm;
	
	@Override
	public void executePhase(LifeCyclePhaseEvent phaseEvent) {
		  RequestContext ctx = createRequestContext(phaseEvent);
		  Timer.Context context = timer.time();
	        try {
				xfsm.go(ctx);
			} catch (AugeBusinessException e){
				throw e;
			}catch(Exception e) {
				throw new AugeSystemException("executeLifeCyclePhase error!",e);
			}finally{
				context.stop();
			}
	}

	private RequestContext createRequestContext(LifeCyclePhaseEvent phaseEvent) {
		 RequestContext ctx = new RequestContext(phaseEvent.getStateMachine());
		  ctx.setContext( phaseEvent.getExtensionInfo());
		 if(phaseEvent.getCurrentState()!=null){
			ctx.setCurrentState(phaseEvent.getCurrentState());
		  }
		  ctx.set("event", phaseEvent.getEvent().getEvent());
		  ctx.set("payload", phaseEvent.getPayload());
	      ctx.events(new XFSMEvent(phaseEvent.getEvent().getEvent(),phaseEvent.getPayload()));
	    
		return ctx;
	}

}
