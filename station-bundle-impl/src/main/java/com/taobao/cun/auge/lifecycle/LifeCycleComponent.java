package com.taobao.cun.auge.lifecycle;

public interface LifeCycleComponent {

	public static final String SETTLING_COMPONENT = "SETTLING";
	
	public static final String DECORATING_COMPONENT = "DECORATING";
	
	public String getComponentName();
	
	public String getPhase();
	
}
