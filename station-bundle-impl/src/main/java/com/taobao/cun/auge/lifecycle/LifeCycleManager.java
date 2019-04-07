package com.taobao.cun.auge.lifecycle;

import com.taobao.cun.auge.lifecycle.common.LifeCyclePhase;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseContext;

public interface LifeCycleManager {

	/**
	 * 入驻业务入口
	 * @param context
	 */
	 void execute(LifeCyclePhaseContext context);
	 
	 
	 /**
	  * 注册LifeCyclePhase
	  * @param phase
	  */
	 void registerLifeCyclePhase(LifeCyclePhase phase);
	 
	 /**
	  * 生命周期服务编排
	  * @param dsl
	  */
     default void executeDSL(LifeCyclePhaseDSL dsl){
    	 dsl.invoke();
     }
     
    
}
