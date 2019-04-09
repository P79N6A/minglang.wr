package com.taobao.cun.auge.lifecycle.event;

import java.util.Map;

import com.taobao.cun.auge.lifecycle.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import org.springframework.util.Assert;

/**
 * 生命周期节点事件
 */
public class LifeCyclePhaseEvent {

    /**
     * 状态机
     */
    private String stateMachine;

    /**
     * 状态机事件
     */
    private StateMachineEvent event;

    /**
     * 当前状态
     */
    private String currentState;

    /**
     * 扩展信息
     */
    private Map<String, Object> extensionInfo;

    public Map<String, Object> getExtensionInfo() {
        return extensionInfo;
    }

    public void setExtensionInfo(Map<String, Object> extensionInfo) {
        this.extensionInfo = extensionInfo;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getStateMachine() {
        return stateMachine;
    }

    public void setStateMachine(String stateMachine) {
        this.stateMachine = stateMachine;
    }

    public StateMachineEvent getEvent() {
        return event;
    }

    public void setEvent(StateMachineEvent event) {
        Assert.notNull(event, "event is null");
        this.event = event;
    }

    private PartnerInstanceDto payload;

    public LifeCyclePhaseEvent() {
    }

    public LifeCyclePhaseEvent(String stateMachine, StateMachineEvent event, PartnerInstanceDto partnerInstanceDto) {
        Assert.notNull(stateMachine, "stateMachine is null");
        Assert.notNull(event, "event is null");
        Assert.notNull(partnerInstanceDto, "partnerInstanceDto is null");
        this.event = event;
        this.stateMachine = stateMachine;
        this.payload = partnerInstanceDto;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(PartnerInstanceDto payload) {
        this.payload = payload;
    }

}
