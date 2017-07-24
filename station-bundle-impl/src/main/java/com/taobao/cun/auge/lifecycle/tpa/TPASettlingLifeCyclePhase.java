package com.taobao.cun.auge.lifecycle.tpa;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.lifecycle.LifeCyclePhaseAdapter;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseDSL;

@Component
public class TPASettlingLifeCyclePhase extends LifeCyclePhaseAdapter{

	private static final String USER_TYPE = "TPA";
	
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

	public void uniqueTpaSettlingBussiness(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doUniqueTpaSettlingBussiness");
	}
	
	
	@Override
	public LifeCyclePhaseDSL createPhaseDSL(LifeCyclePhaseContext context){
			LifeCyclePhaseDSL dsl = new LifeCyclePhaseDSL(context);
			 dsl.then(this::createOrUpdateStation);
			 dsl.then(this::createOrUpdatePartner);
			 dsl.then(this::createOrUpdatePartnerInstance);
			 dsl.then(this::createOrUpdatePartnerInstance);
			 dsl.then(this::uniqueTpaSettlingBussiness);
			// dsl.then(this::createOrUpdateLifeCycleItems);
			// dsl.then(this::createOrUpdateExtensionBusiness);
			// dsl.then(this::syncStationApply);
			 return dsl;
	}
	 
	@Override
	public String getComponentName() {
		return USER_TYPE+"_"+getPhase()+"_EVENT";
	}

	@Override
	public String getPhase() {
		return "SETTLING";
	}

}
