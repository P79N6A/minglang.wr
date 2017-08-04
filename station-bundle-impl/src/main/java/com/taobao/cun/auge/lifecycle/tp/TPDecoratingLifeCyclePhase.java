package com.taobao.cun.auge.lifecycle.tp;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;

/**
 * 合伙人入驻中阶段组件
 * @author zhenhuan.zhangzh
 *
 */
@Component
@Phase(type="TP",event=StateMachineEvent.DECORATING_EVENT,desc="村小二入装修中节点服务")
public class TPDecoratingLifeCyclePhase extends AbstractLifeCyclePhase{

	@Override
	
	@PhaseStepMeta(descr="更新村点信息")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		//System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doCreateOrUpdateStation");
	}

	@Override
	@PhaseStepMeta(descr="更新合伙人信息")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doCreateOrUpdatePartner");
	}

	@Override
	@PhaseStepMeta
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doCreateOrUpdatePartnerInstance");
	}

	@Override
	@PhaseStepMeta
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doCreateOrUpdateLifeCycleItems");
	}

	@Override
	@PhaseStepMeta
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doCreateOrUpdateExtensionBusiness");
	}

	@Override
	@PhaseStepMeta
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doTriggerLifeCycleChangeEvent");
	}

	@Override
	@PhaseStepMeta
	public void syncStationApply(LifeCyclePhaseContext context) {
		System.err.println(context.getUserType()+":"+context.getEvent()+":"+"doSyncStationApply");
	}


}
