package com.taobao.cun.auge.lifecycle;

public interface LifeCyclePhaseComponent {
	
	public PhaseKey getPhaseKey();
	
	LifeCyclePhaseDSL  getDsl();
	
	void setDsl(LifeCyclePhaseDSL dsl);
}
