package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;
import com.taobao.cun.auge.common.PageDto;

/**
 * Created by xiao on 17/5/17.
 */
public class AreaAssetDetailDto implements Serializable{

    private static final long serialVersionUID = -6206421493881158034L;

    private String ownerArea;

    private String owner;

    private List<AssetCategoryCountDto> categoryCountDtoList;

    private PageDto<AssetDetailDto> detailList;

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

    public List<AssetCategoryCountDto> getCategoryCountDtoList() {
        return categoryCountDtoList;
    }

    public void setCategoryCountDtoList(List<AssetCategoryCountDto> categoryCountDtoList) {
        this.categoryCountDtoList = categoryCountDtoList;
    }

    public PageDto<AssetDetailDto> getDetailList() {
        return detailList;
    }

    public void setDetailList(PageDto<AssetDetailDto> detailList) {
        this.detailList = detailList;
    }
}
