package com.taobao.cun.auge.punish.dto;

import java.io.Serializable;

/**
 * 违规处罚信息Dto
 */
public class ViolationPunishInfoDto implements Serializable {


    private static final long serialVersionUID = 9054019754592940763L;

    //村淘一般违规扣分数
    private Double generalViolationPoints;

    //村淘严重违规扣分数
    private Double seriousViolationPoints;

    //违规总数
    private Integer totalIllegalCount;

    public Double getGeneralViolationPoints() {
        return generalViolationPoints;
    }

    public void setGeneralViolationPoints(Double generalViolationPoints) {
        this.generalViolationPoints = generalViolationPoints;
    }

    public Double getSeriousViolationPoints() {
        return seriousViolationPoints;
    }

    public void setSeriousViolationPoints(Double seriousViolationPoints) {
        this.seriousViolationPoints = seriousViolationPoints;
    }

    public Integer getTotalIllegalCount() {
        return totalIllegalCount;
    }

    public void setTotalIllegalCount(Integer totalIllegalCount) {
        this.totalIllegalCount = totalIllegalCount;
    }

}
