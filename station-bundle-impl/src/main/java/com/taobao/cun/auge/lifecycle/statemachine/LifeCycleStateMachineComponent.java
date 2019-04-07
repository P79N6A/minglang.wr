package com.taobao.cun.auge.lifecycle.statemachine;

import com.alibaba.shared.xfsm.core.annotation.Inject;
import com.alibaba.shared.xfsm.core.context.RequestContext;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseManager;
import com.taobao.cun.auge.statemachine.StateMachineComponent;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import org.springframework.beans.factory.annotation.Autowired;

@StateMachineComponent(stateMachine = {"TPStateMachine", "TPAStateMachine", "TPTStateMachine", "TPSStateMachine", "UMStateMachine"}, actionKey = "stateMachineComponent")
public class LifeCycleStateMachineComponent {

    @Autowired
    private LifeCyclePhaseManager lifeCycleManager;

    private LifeCyclePhaseContext createContext(PartnerInstanceDto partnerInstanceDto, RequestContext ctx) {
        LifeCyclePhaseContext context = new LifeCyclePhaseContext();
        context.setPartnerInstance(partnerInstanceDto);
        context.setEvent(ctx.get("event"));

        context.setSourceState(partnerInstanceDto.getState() != null ? partnerInstanceDto.getState().getCode() : "NEW");
        context.setTargetState(StateMachineEvent.valueOfEvent(context.getEvent()).getState());
        context.setUserType(partnerInstanceDto.getType().getCode());
        context.setExtensionInfos(ctx.context());
        return context;
    }

    public void settling(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx) {
        LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
        lifeCycleManager.execute(context);
    }

    public void decorating(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx) {
        LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
        lifeCycleManager.execute(context);
    }


    public void servicing(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx) {
        LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
        lifeCycleManager.execute(context);
    }


    public void closing(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx) {
        LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
        lifeCycleManager.execute(context);
    }

    public void closed(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx) {
        LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
        lifeCycleManager.execute(context);
    }

    public void quiting(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx) {
        LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
        lifeCycleManager.execute(context);
    }

    public void quit(@Inject("payload") PartnerInstanceDto partnerInstanceDto, RequestContext ctx) {
        LifeCyclePhaseContext context = createContext(partnerInstanceDto, ctx);
        lifeCycleManager.execute(context);
    }
}
