package com.taobao.cun.auge.lifecycle;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
		this.lifeCycleComponents.putIfAbsent(phase.getPhaseKey().getKey(), phase);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.lifeCyclePhases != null && !this.lifeCyclePhases.isEmpty()){
			this.lifeCyclePhases.stream().forEach(this::registerLifeCyclePhase);
		}
	}

}
