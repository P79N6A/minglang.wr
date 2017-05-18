package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.asset.enums.AssetStatusEnum;

/**
 * Created by xiao on 17/5/17.
 */
public class AreaAssetListDto implements Serializable{

    private static final long serialVersionUID = -5546874145184714228L;

    private String ownerArea;

    private String owner;

    private String useArea;

    private Long useAreaId;

    private String useAreaType;

    private List<AssetCategoryCountDto> countList;

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

    public String getUseArea() {
        return useArea;
    }

    public void setUseArea(String useArea) {
        this.useArea = useArea;
    }

    public Long getUseAreaId() {
        return useAreaId;
    }

    public void setUseAreaId(Long useAreaId) {
        this.useAreaId = useAreaId;
    }

    public String getUseAreaType() {
        return useAreaType;
    }

    public void setUseAreaType(String useAreaType) {
        this.useAreaType = useAreaType;
    }

    public List<AssetCategoryCountDto> getCountList() {
        return countList;
    }

    public void setCountList(List<AssetCategoryCountDto> countList) {
        this.countList = countList;
    }

    public Map<AssetStatusEnum, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<AssetStatusEnum, String> statusMap) {
        this.statusMap = statusMap;
    }
}
