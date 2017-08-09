package com.taobao.cun.auge.opensearch;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class OrginOpenSearchReturnVariableValue {
    @JSONField(name="extinfo")
    private List<String> extInfo;

    public List<String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(List<String> extInfo) {
        this.extInfo = extInfo;
    }
}
