package com.taobao.cun.auge.lifecycle;

/**
 * 人村生命周期管理接口，结合状态机使用
 * @author zhenhuan.zhangzh
 *
 */
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
	  * @param promise
	  * @param phase
	  */
     default void executeDSL(LifeCyclePhaseDSL dsl){
    	 dsl.invoke();
     }
     
    
}
