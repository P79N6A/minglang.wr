package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.Map;

import com.taobao.cun.auge.asset.enums.AssetStatusEnum;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetCategoryCountDto implements Serializable{

    private static final long serialVersionUID = 6998191282561709835L;

    private String category;

    private String categoryName;

    private String total;

    private String putAway;

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
