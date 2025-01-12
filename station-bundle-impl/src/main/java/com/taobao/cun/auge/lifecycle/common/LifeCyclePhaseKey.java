package com.taobao.cun.auge.lifecycle.common;


public class LifeCyclePhaseKey {

    private static final String SPLIT = "_";

    private String userType;

    private String event;

    public LifeCyclePhaseKey(String userType, String event) {
        this.userType = userType;
        this.event = event;
    }

    public String getKey() {
        return userType + SPLIT + event;
    }
}
