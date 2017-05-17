package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiao on 17/5/17.
 */
public class AreaAssetDetailDto implements Serializable{

    private static final long serialVersionUID = -6206421493881158034L;

    private String dutyArea;

    private String dutyUser;

    private String useArea;

    private String waitRecycle;

    private String waitIncome;

    private List<AssetCountDto> countList;

    private List<AssetDetailDto> detailList;

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

    public String getUseArea() {
        return useArea;
    }

    public void setUseArea(String useArea) {
        this.useArea = useArea;
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

    public List<AssetCountDto> getCountList() {
        return countList;
    }

    public void setCountList(List<AssetCountDto> countList) {
        this.countList = countList;
    }

    public List<AssetDetailDto> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<AssetDetailDto> detailList) {
        this.detailList = detailList;
    }
}
