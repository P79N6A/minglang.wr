package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.PageDto;

/**
 * Created by xiao on 17/5/17.
 */
public class CategoryAssetDetailDto implements Serializable{

    private static final long serialVersionUID = -1296530323443907933L;

    private String category;

    private String categoryName;

    private String putAway;

    private String ownerArea;

    private String owner;

    private String total;

    private PageDto<AssetDetailDto> detailList;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPutAway() {
        return putAway;
    }

    public void setPutAway(String putAway) {
        this.putAway = putAway;
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

    public PageDto<AssetDetailDto> getDetailList() {
        return detailList;
    }

    public void setDetailList(PageDto<AssetDetailDto> detailList) {
        this.detailList = detailList;
    }
}
