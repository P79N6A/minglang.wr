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

    private String total;

    private Map<AssetStatusEnum, String> statusMap;

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
