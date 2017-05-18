package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.asset.enums.AssetStatusEnum;
import com.taobao.cun.auge.client.page.DefaultPageResult;

/**
 * Created by xiao on 17/5/17.
 */
public class CategoryAssetDetailDto implements Serializable{

    private static final long serialVersionUID = -1296530323443907933L;

    private String category;

    private Map<AssetStatusEnum, String> statusMap;

    private String ownerArea;

    private String owner;

    private String total;

    private DefaultPageResult<AssetDetailDto> detailList;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<AssetStatusEnum, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<AssetStatusEnum, String> statusMap) {
        this.statusMap = statusMap;
    }

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public DefaultPageResult<AssetDetailDto> getDetailList() {
        return detailList;
    }

    public void setDetailList(
        DefaultPageResult<AssetDetailDto> detailList) {
        this.detailList = detailList;
    }
}
