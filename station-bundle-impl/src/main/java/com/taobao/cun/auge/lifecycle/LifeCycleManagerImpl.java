package com.taobao.cun.auge.lifecycle;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ali.com.google.common.collect.Maps;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

@Component
public class LifeCycleManagerImpl implements LifeCycleManager,InitializingBean{

	Map<String,LifeCyclePhase> lifeCycleComponents = Maps.newConcurrentMap();
	
	@Autowired
	private List<LifeCyclePhase> lifeCyclePhases;
	
	public LifeCyclePhase getLifeCyclePhase(String componentIndentity){
		Assert.notNull(lifeCycleComponents.get(componentIndentity));
		return lifeCycleComponents.get(componentIndentity);
	}
	
	@Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void execute(LifeCyclePhaseContext context) {
		LifeCyclePhase phase = getLifeCyclePhase(context.getPhaseKey().getKey());
		try {
			LifeCyclePhaseContextHolder.setContext(context);
			executeDSL(phase.createPhaseDSL());
		} finally{
			LifeCyclePhaseContextHolder.clean();
		}
	
	}


	@Override
	public void registerLifeCyclePhase(LifeCyclePhase phase) {
		collectRuntimMetaInfo(phase);
		this.lifeCycleComponents.putIfAbsent(phase.getPhaseKey().getKey(), phase);
	}

	private void collectRuntimMetaInfo(LifeCyclePhase phase) {
		Enhancer enhancer =new Enhancer();  
		CallbackFilter callbackFilter = new TargetMethodCallbackFilter();  
		Callback[] cbarray=new Callback[]{NoOp.INSTANCE,new LifeCyclePhaseInterceptor()};  
		enhancer.setCallbackFilter(callbackFilter);
        enhancer.setSuperclass(AopUtils.getTargetClass(phase));  
        enhancer.setCallbacks(cbarray);  
        LifeCyclePhase proxyPhase=(LifeCyclePhase)enhancer.create();  
        LifeCyclePhaseContextHolder.setContext(new LifeCyclePhaseContext());
        proxyPhase.createPhaseDSL().invoke();
        LifeCyclePhaseContextHolder.clean();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.lifeCyclePhases != null && !this.lifeCyclePhases.isEmpty()){
			this.lifeCyclePhases.stream().forEach(this::registerLifeCyclePhase);
		}
	}

	
	static class LifeCyclePhaseInterceptor implements MethodInterceptor{

		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			RuntimeMetaInfoCollector.collectMetaInfo(method.getDeclaringClass(),method);
			return null;
		}
		
	}
	
	static class TargetMethodCallbackFilter implements CallbackFilter{

		@Override
		public int accept(Method method) {
			PhaseStepMeta meta = AnnotationUtils.getAnnotation(method, PhaseStepMeta.class);
			if(meta == null){
				 return 0;
			}
			else{
				return 1;
			}
		}
		
	}
}
