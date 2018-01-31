package com.taobao.cun.auge.opensearch;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

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
