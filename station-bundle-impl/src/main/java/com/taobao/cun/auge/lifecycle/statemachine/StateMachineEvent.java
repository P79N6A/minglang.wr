package com.taobao.cun.auge.lifecycle.statemachine;

import java.util.stream.Stream;

import org.springframework.util.Assert;

public enum StateMachineEvent {
    /**
     * 入驻中
     */
    SETTLING_EVENT("SETTLING_EVENT", "SETTLING"),
    /**
     * 装修中
     */
    DECORATING_EVENT("DECORATING_EVENT", "DECORATING"),
    /**
     * 服务中
     */
    SERVICING_EVENT("SERVICING_EVENT", "SERVICING"),
    /**
     * 停业申请中
     */
    CLOSING_EVENT("CLOSING_EVENT", "CLOSING"),
    /**
     * 已停业
     */
    CLOSED_EVENT("CLOSED_EVENT", "CLOSED"),
    /**
     * 退出申请中
     */
    QUITING_EVENT("QUITING_EVENT", "QUITING"),
    /**
     * 已退出
     */
    QUIT_EVENT("QUIT_EVENT", "QUIT");

    private String event;
    private String state;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private StateMachineEvent(String event, String state) {
        this.event = event;
        this.state = state;
    }

    public static StateMachineEvent valueOfState(String state) {
        StateMachineEvent sme = Stream.of(StateMachineEvent.values()).filter(value -> value.getState().equals(state)).findFirst().orElse(null);
        Assert.notNull(sme, "StateMachineEvent not exist");
        return sme;
    }

    public static StateMachineEvent valueOfEvent(String event) {
        StateMachineEvent sme = Stream.of(StateMachineEvent.values()).filter(value -> value.getEvent().equals(event)).findFirst().orElse(null);
        Assert.notNull(sme, "StateMachineEvent not exist");
        return sme;
    }


}
