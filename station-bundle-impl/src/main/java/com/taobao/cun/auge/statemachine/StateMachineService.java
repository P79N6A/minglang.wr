package com.taobao.cun.auge.statemachine;

import com.taobao.cun.auge.lifecycle.event.LifeCyclePhaseEvent;

public interface StateMachineService {
	 void executePhase(LifeCyclePhaseEvent phaseEvent);
}
