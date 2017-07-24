package com.taobao.cun.auge.lifecycle;

import static io.advantageous.reakt.promise.Promises.invokablePromise;

import java.util.List;
import java.util.function.Consumer;

import com.ali.com.google.common.collect.Lists;

import io.advantageous.reakt.promise.Promise;
import io.advantageous.reakt.promise.Promises;

/**
 * 生命周期DSL定义，通过DSL来编排业务流程
 * @author zhenhuan.zhangzh
 *
 */
public class LifeCyclePhaseDSL {

	 private List<Promise<LifeCyclePhaseContext>> promises= Lists.newArrayList();
	 
	 private LifeCyclePhaseContext context;
	 
	 public LifeCyclePhaseDSL(LifeCyclePhaseContext context){
		 this.context = context;
	 }
	 
	 Promise<LifeCyclePhaseContext> createPromise(LifeCyclePhaseContext context) {
         return invokablePromise(promise -> {
             if (context == null) {
                 promise.reject("LifeCyclePhaseContext was null");
             } else {
                 promise.resolve(context);
             }
         });
     }
	 
	 
	public LifeCyclePhaseDSL then(Consumer<LifeCyclePhaseContext> consumer){
		 promises.add(createPromise(context).then(consumer).asPromise());
		 return this;
	}
	
	public void invoke(){
		Promises.all(promises).invoke();
	}
}
