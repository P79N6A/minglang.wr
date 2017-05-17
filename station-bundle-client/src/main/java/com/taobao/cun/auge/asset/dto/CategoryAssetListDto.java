package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

/**
 * Created by xiao on 17/5/17.
 */
public class CategoryAssetListDto implements Serializable{

    private static final long serialVersionUID = -2800218032622909374L;

    private String dutyArea;

    private String dutyUser;

    private String category;

    private String total;

    private String waitRecycle;

    private String waitIncome;

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
}
