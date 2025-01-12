package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetDetailQueryCondition extends AssetOperatorDto implements Serializable{

    private static final long serialVersionUID = 2282592861027828317L;

    /**
     * county || station
     */
    private String useAreaType;

    private String category;

    private Long useAreaId;

    private String status;

    private String aliNo;

    public String getUseAreaType() {
        return useAreaType;
    }

    public void setUseAreaType(String useAreaType) {
        this.useAreaType = useAreaType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getUseAreaId() {
        return useAreaId;
    }

    public void setUseAreaId(Long useAreaId) {
        this.useAreaId = useAreaId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAliNo() {
        return aliNo;
    }

    public void setAliNo(String aliNo) {
        this.aliNo = aliNo;
    }
}
