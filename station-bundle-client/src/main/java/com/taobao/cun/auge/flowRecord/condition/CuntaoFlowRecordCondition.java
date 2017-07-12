package com.taobao.cun.auge.flowRecord.condition;

import java.io.Serializable;

public class CuntaoFlowRecordCondition implements Serializable {

    private static final long serialVersionUID = -5877224183669147352L;

    private Long targetId;

    private String targetType;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
