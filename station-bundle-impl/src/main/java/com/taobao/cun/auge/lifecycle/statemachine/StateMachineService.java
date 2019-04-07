package com.taobao.cun.auge.lifecycle.statemachine;

import com.taobao.cun.auge.lifecycle.event.LifeCyclePhaseEvent;

public interface StateMachineService {
    /**
     * 执行生命周期节点事件
     * @param phaseEvent
     */
    void executePhase(LifeCyclePhaseEvent phaseEvent);
}
