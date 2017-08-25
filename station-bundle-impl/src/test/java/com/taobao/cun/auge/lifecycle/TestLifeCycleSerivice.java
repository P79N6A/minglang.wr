package com.taobao.cun.auge.lifecycle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.taobao.cun.auge.lifecycle.tp.TPDecoratingLifeCyclePhase;

@RunWith(SpringRunner.class)
public class TestLifeCycleSerivice {

	@Test
	public void testTpDecorating(){
		LifeCycleManager lifeCycleManager = new LifeCycleManagerImpl();
		registerPhase(lifeCycleManager);
		LifeCyclePhaseContext context = createContext();
		lifeCycleManager.execute(context);
	}

	private void registerPhase(LifeCycleManager lifeCycleManager) {
		LifeCyclePhase phase = new TPDecoratingLifeCyclePhase();
		lifeCycleManager.registerLifeCyclePhase(phase);
	}

	private LifeCyclePhaseContext createContext() {
		LifeCyclePhaseContext context = new LifeCyclePhaseContext();
		context.setUserType("TP");
		context.setEvent("DECORATING_EVENT");
		return context;
	}
	
  
}
