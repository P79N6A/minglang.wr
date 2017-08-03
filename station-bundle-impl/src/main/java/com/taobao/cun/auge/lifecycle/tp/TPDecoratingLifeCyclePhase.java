package com.taobao.cun.auge.lifecycle.tp;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.statemachine.StateMachineEvent;

/**
 * 合伙人入驻中阶段组件
 * @author zhenhuan.zhangzh
 *
 */
@Component
@Phase(type="TP",event=StateMachineEvent.DECORATING_EVENT)
public class TPDecoratingLifeCyclePhase extends AbstractLifeCyclePhase{

	private static final String USER_TYPE = "TP";
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


}
