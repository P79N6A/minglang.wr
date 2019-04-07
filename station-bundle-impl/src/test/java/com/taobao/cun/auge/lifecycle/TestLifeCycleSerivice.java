package com.taobao.cun.auge.lifecycle;

import com.taobao.cun.auge.lifecycle.common.LifeCyclePhase;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseManager;
import com.taobao.cun.auge.lifecycle.tp.BaseLifeCyclePhase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TestLifeCycleSerivice {

	@Test
	public void testTpDecorating(){
		LifeCyclePhaseManager lifeCycleManager = new LifeCyclePhaseManager();
		registerPhase(lifeCycleManager);
		LifeCyclePhaseContext context = createContext();
		lifeCycleManager.execute(context);
	}

	private void registerPhase(LifeCyclePhaseManager lifeCycleManager) {
		LifeCyclePhase phase = new BaseLifeCyclePhase();
		lifeCycleManager.registerLifeCyclePhase(phase);
	}

	private LifeCyclePhaseContext createContext() {
		LifeCyclePhaseContext context = new LifeCyclePhaseContext();
		context.setUserType("TP");
		context.setEvent("DECORATING_EVENT");
		return context;
	}
	
  
}
