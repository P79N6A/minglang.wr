package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiao on 17/5/17.
 */
public class CategoryAssetDetailDto implements Serializable{

    private static final long serialVersionUID = -1296530323443907933L;

    private String category;

    private String waitRecycle;

    private String waitIncome;

    private String dutyArea;

    private String dutyUser;

    private String total;

    private List<AssetDetailDto> detailList;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWaitRecycle() {
        return waitRecycle;
    }

    public void setWaitRecycle(String waitRecycle) {
        this.waitRecycle = waitRecycle;
    }

    public String getWaitIncome() {
        return waitIncome;
    }

    public void setWaitIncome(String waitIncome) {
        this.waitIncome = waitIncome;
    }

    public String getDutyArea() {
        return dutyArea;
    }

    public void setDutyArea(String dutyArea) {
        this.dutyArea = dutyArea;
    }

    public String getDutyUser() {
        return dutyUser;
    }

    public void setDutyUser(String dutyUser) {
        this.dutyUser = dutyUser;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<AssetDetailDto> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<AssetDetailDto> detailList) {
        this.detailList = detailList;
    }
}
