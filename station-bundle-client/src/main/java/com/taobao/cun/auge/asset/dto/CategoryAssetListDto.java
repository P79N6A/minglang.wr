package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.Map;

import com.taobao.cun.auge.asset.enums.AssetStatusEnum;

/**
 * Created by xiao on 17/5/17.
 */
public class CategoryAssetListDto implements Serializable{

    private static final long serialVersionUID = -2800218032622909374L;

    private String ownerArea;

    private String owner;

    private String category;

    private String total;

    private Map<AssetStatusEnum, String> statusMap;

    public String getOwnerArea() {
        return ownerArea;
    }

    public void setOwnerArea(String ownerArea) {
        this.ownerArea = ownerArea;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Map<AssetStatusEnum, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<AssetStatusEnum, String> statusMap) {
        this.statusMap = statusMap;
    }
}
