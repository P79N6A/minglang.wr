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
	 void settling(LifeCyclePhaseContext context);
	 
	 /**
	  * 装修业务入口
	  * @param context
	  */
	 void decorating(LifeCyclePhaseContext context);
	 
	 /**
	  * 开业业务入口
	  * @param context
	  */
	 void servicing(LifeCyclePhaseContext context);
	 
	 /**
	  * 停业申请业务入口
	  * @param context
	  */
	 void closing(LifeCyclePhaseContext context);
	
	 /**
	  * 停业业务入口
	  * @param context
	  */
	 void closed(LifeCyclePhaseContext context);
	 
	 /**
	  * 退出申请业务入口
	  * @param context
	  */
	 void quiting(LifeCyclePhaseContext context);
	 
	 /**
	  * 退出业务入口
	  * @param context
	  */
	 void quit(LifeCyclePhaseContext context);
	 
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
