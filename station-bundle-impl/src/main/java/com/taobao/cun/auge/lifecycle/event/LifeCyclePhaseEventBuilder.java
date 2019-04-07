package com.taobao.cun.auge.lifecycle.event;

import java.util.Map;

import com.taobao.cun.auge.lifecycle.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import org.springframework.util.Assert;


public class LifeCyclePhaseEventBuilder {

    /**
     * 构建状态及事件
     *
     * @param partnerInstanceDto 站点实例信息
     * @param smEvent            事件
     * @param extensionInfo      扩展信息
     * @return
     */
    public static LifeCyclePhaseEvent build(PartnerInstanceDto partnerInstanceDto, StateMachineEvent smEvent, Map<String, Object> extensionInfo) {
        Assert.notNull(partnerInstanceDto, "partnerInstance is null");
        LifeCyclePhaseEvent event = new LifeCyclePhaseEvent();
        event.setStateMachine(partnerInstanceDto.getType().getCode() + "StateMachine");
        if (partnerInstanceDto.getState() != null) {
            event.setCurrentState(partnerInstanceDto.getState().getCode());
        } else if (smEvent.getEvent().equals(StateMachineEvent.SETTLING_EVENT.getEvent())) {
            event.setCurrentState("NEW");
        }
        event.setEvent(smEvent);
        event.setPayload(partnerInstanceDto);
        if (extensionInfo != null) {
            event.setExtensionInfo(extensionInfo);
        }
        return event;
    }

    public static LifeCyclePhaseEvent build(PartnerInstanceDto partnerInstanceDto, StateMachineEvent smEvent) {
        return build(partnerInstanceDto, smEvent, null);
    }
}
