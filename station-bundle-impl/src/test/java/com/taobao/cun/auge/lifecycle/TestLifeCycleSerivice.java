package com.taobao.cun.auge.lifecycle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.taobao.cun.auge.lifecycle.tp.TPSettlingLifeCyclePhase;

@RunWith(SpringRunner.class)
public class TestLifeCycleSerivice {

	@Test
	public void testTpSettling(){
		LifeCycleManager lifeCycleManager = new LifeCycleManagerImpl();
		registerPhase(lifeCycleManager);
		LifeCyclePhaseContext context = createContext();
		lifeCycleManager.settling(context);
	}

	private void registerPhase(LifeCycleManager lifeCycleManager) {
		LifeCyclePhase phase = new TPSettlingLifeCyclePhase();
		lifeCycleManager.registerLifeCyclePhase(phase);
	}

	private LifeCyclePhaseContext createContext() {
		LifeCyclePhaseContext context = new LifeCyclePhaseContext();
		context.setUserType("TP");
		context.setEvent("SETTLING_EVENT");
		return context;
	}
	
  
}
