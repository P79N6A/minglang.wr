package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiao on 17/5/17.
 */
public class AreaAssetListDto implements Serializable{

    private static final long serialVersionUID = -5546874145184714228L;

    private String dutyArea;

    private String dutyUser;

    private String useArea;

    private List<AssetCountDto> countList;

    private String waitRecycle;

    private String waitIncome;

    private Long useAreaId;

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

    public Long getUseAreaId() {
        return useAreaId;
    }

    public void setUseAreaId(Long useAreaId) {
        this.useAreaId = useAreaId;
    }

    public List<AssetCountDto> getCountList() {
        return countList;
    }

    public void setCountList(List<AssetCountDto> countList) {
        this.countList = countList;
    }
}
