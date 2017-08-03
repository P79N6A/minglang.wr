package com.taobao.cun.auge.lifecycle.tpa;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.lifecycle.LifeCyclePhaseAdapter;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseDSL;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.statemachine.StateMachineEvent;

@Component
@Phase(type="TPA",event=StateMachineEvent.SETTLING_EVENT)
public class TPAServicingLifeCyclePhase extends LifeCyclePhaseAdapter{

	
	@Override
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doCreateOrUpdateStation");
	}

	@Override
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doCreateOrUpdatePartner");
	}

	@Override
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doCreateOrUpdatePartnerInstance");
	}

	@Override
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doCreateOrUpdateLifeCycleItems");
	}

	@Override
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doCreateOrUpdateExtensionBusiness");
	}

	@Override
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doTriggerLifeCycleChangeEvent");
	}

	@Override
	public void syncStationApply(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doSyncStationApply");
	}

	@Override
	public LifeCyclePhaseDSL createPhaseDSL(LifeCyclePhaseContext context){
			LifeCyclePhaseDSL dsl = new LifeCyclePhaseDSL(context);
			 dsl.then(this::createOrUpdateStation);
			 dsl.then(this::createOrUpdatePartner);
			 dsl.then(this::createOrUpdatePartnerInstance);
			 dsl.then(this::createOrUpdateLifeCycleItems);
			// dsl.then(this::createOrUpdateExtensionBusiness);
			// dsl.then(this::syncStationApply);
			 return dsl;
	}
	 

}
