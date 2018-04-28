package com.taobao.cun.auge.statemachine;

import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;

public interface StateMachineService {
	 void executePhase(LifeCyclePhaseEvent phaseEvent);
}
