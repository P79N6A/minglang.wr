package com.taobao.cun.auge.opensearch;

import com.alibaba.fastjson.annotation.JSONField;

public class OrginOpenSearchReturnExtInfo {
    @JSONField(name="extinfo")
    private String extinfo;

    public String getExtinfo() {
        return extinfo;
    }

    public void setExtinfo(String extinfo) {
        this.extinfo = extinfo;
    }
}
