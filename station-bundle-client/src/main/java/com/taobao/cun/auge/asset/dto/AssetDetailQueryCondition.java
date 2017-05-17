package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetDetailQueryCondition implements Serializable{

    private static final long serialVersionUID = 2282592861027828317L;

    private String userId;

    /**
     * county || station
     */
    private String areaType;

    private String category;

    private String useAreaId;

    private String checkStatus;

    private String status;

    private String aliNo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUseAreaId() {
        return useAreaId;
    }

    public void setUseAreaId(String useAreaId) {
        this.useAreaId = useAreaId;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
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
