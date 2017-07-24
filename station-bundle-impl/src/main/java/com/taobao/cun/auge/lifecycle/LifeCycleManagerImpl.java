package com.taobao.cun.auge.lifecycle;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ali.com.google.common.collect.Maps;

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
	public void settling(LifeCyclePhaseContext context) {
		LifeCyclePhase phase = getLifeCyclePhase(context.getComponentyIdentity());
		executeDSL(phase.createPhaseDSL(context));
	}

	@Override
	public void decorating(LifeCyclePhaseContext context) {
		LifeCyclePhase phase = getLifeCyclePhase(context.getComponentyIdentity());
		executeDSL(phase.createPhaseDSL(context));
	}

	@Override
	public void servicing(LifeCyclePhaseContext context) {
		LifeCyclePhase phase = getLifeCyclePhase(context.getComponentyIdentity());
		executeDSL(phase.createPhaseDSL(context));
	}
	
	
	@Override
	public void closing(LifeCyclePhaseContext context) {
		LifeCyclePhase phase = getLifeCyclePhase(context.getComponentyIdentity());
		executeDSL(phase.createPhaseDSL(context));
	}

	@Override
	public void closed(LifeCyclePhaseContext context) {
		LifeCyclePhase phase = getLifeCyclePhase(context.getComponentyIdentity());
		executeDSL(phase.createPhaseDSL(context));
	}

	@Override
	public void quiting(LifeCyclePhaseContext context) {
		LifeCyclePhase phase = getLifeCyclePhase(context.getComponentyIdentity());
		executeDSL(phase.createPhaseDSL(context));
	}

	@Override
	public void quit(LifeCyclePhaseContext context) {
		LifeCyclePhase phase = getLifeCyclePhase(context.getComponentyIdentity());
		executeDSL(phase.createPhaseDSL(context));
	}

	@Override
	public void registerLifeCyclePhase(LifeCyclePhase phase) {
		this.lifeCycleComponents.putIfAbsent(phase.getComponentName(), phase);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.lifeCyclePhases != null && !this.lifeCyclePhases.isEmpty()){
			this.lifeCyclePhases.stream().forEach(this::registerLifeCyclePhase);
		}
	}

}
