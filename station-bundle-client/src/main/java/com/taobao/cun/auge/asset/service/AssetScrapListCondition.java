package com.taobao.cun.auge.asset.service;

import java.io.Serializable;

import com.taobao.cun.auge.asset.dto.AssetOperatorDto;

/**
 * Created by xiao on 17/6/7.
 */
public class AssetScrapListCondition extends AssetOperatorDto implements Serializable {

    private static final long serialVersionUID = 7267109830157934291L;

    private String useAreaType;

    private Long useAreaId;

    public String getUseAreaType() {
        return useAreaType;
    }

    public void setUseAreaType(String useAreaType) {
        this.useAreaType = useAreaType;
    }

    public Long getUseAreaId() {
        return useAreaId;
    }

    public void setUseAreaId(Long useAreaId) {
        this.useAreaId = useAreaId;
    }
}
