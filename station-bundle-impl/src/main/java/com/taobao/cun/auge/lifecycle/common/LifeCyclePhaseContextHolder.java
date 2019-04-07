package com.taobao.cun.auge.lifecycle.common;


import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseContext;

public class LifeCyclePhaseContextHolder {

	private static final ThreadLocal<LifeCyclePhaseContext> CONTEXT_THREAD_LOCAL = new ThreadLocal<LifeCyclePhaseContext>();

	public static final void setContext(LifeCyclePhaseContext context){
		CONTEXT_THREAD_LOCAL.set(context);
	}
	
	public static final LifeCyclePhaseContext getContext(){
		return CONTEXT_THREAD_LOCAL.get();
	}
	
	public static final void clean(){
		CONTEXT_THREAD_LOCAL.remove();
	}
}
