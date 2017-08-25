package com.taobao.cun.auge.opensearch;

import com.alibaba.fastjson.annotation.JSONField;

public class BaseOpenSearchListVo {
    @JSONField(name="variableValue")
    protected OrginOpenSearchReturnVariableValue variableValue;

    public OrginOpenSearchReturnVariableValue getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(OrginOpenSearchReturnVariableValue variableValue) {
        this.variableValue = variableValue;
    }
}
