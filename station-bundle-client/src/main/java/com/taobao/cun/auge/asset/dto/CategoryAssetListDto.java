package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

/**
 * Created by xiao on 17/5/17.
 */
public class CategoryAssetListDto implements Serializable{

    private static final long serialVersionUID = -2800218032622909374L;

    private String ownerArea;

    private String owner;

    private String category;

    private String categoryName;

    private String total;

    private String putAway;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPutAway() {
        return putAway;
    }

    public void setPutAway(String putAway) {
        this.putAway = putAway;
    }
}
