package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class StepInfo implements Serializable{

    private static final long serialVersionUID = -4612518580397921098L;
    private String methodName;

    private String desc;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
