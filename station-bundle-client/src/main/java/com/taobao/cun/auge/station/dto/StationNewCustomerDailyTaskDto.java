package com.taobao.cun.auge.station.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 门店新用户任务表
 *
 * @author haihu.fhh
 */
public class StationNewCustomerDailyTaskDto implements Serializable {

    private static final long serialVersionUID = -3414974273909149856L;

    @NotNull(message = "taobaoUserId is null")
    private Long taobaoUserId;

    private String mobile;

    private Long stationId;

    private String adzoneId;

    private String preInterestTime;

    private String realInterestTime;

    private String finishCpaTime;

    private String cpaConstraintTime;

    private Long status;

    private String risk;

    private String ms;

    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getAdzoneId() {
        return adzoneId;
    }

    public void setAdzoneId(String adzoneId) {
        this.adzoneId = adzoneId;
    }

    public String getPreInterestTime() {
        return preInterestTime;
    }

    public void setPreInterestTime(String preInterestTime) {
        this.preInterestTime = preInterestTime;
    }

    public String getRealInterestTime() {
        return realInterestTime;
    }

    public void setRealInterestTime(String realInterestTime) {
        this.realInterestTime = realInterestTime;
    }

    public String getFinishCpaTime() {
        return finishCpaTime;
    }

    public void setFinishCpaTime(String finishCpaTime) {
        this.finishCpaTime = finishCpaTime;
    }

    public String getCpaConstraintTime() {
        return cpaConstraintTime;
    }

    public void setCpaConstraintTime(String cpaConstraintTime) {
        this.cpaConstraintTime = cpaConstraintTime;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }
}
