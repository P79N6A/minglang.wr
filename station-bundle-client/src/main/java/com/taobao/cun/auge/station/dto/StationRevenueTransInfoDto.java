package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

public class StationRevenueTransInfoDto implements Serializable {

    private String taobaoUserId;

    /**
     * 对应村点ID
     */
    private Long stationId;


    /**
     * 村点类型
     */
    private String type;


    /**
     * 收入切换状态
     * com.taobao.cun.auge.station.enums.PartnerInstanceRevenueStatusEnum;
     */
    private String status;

    /**
     * 新收入类型
     */
    private String incomeMode;

    /**
     * 新收入生效时间
     */
    private Date incomeModeBeginTime;


    /**
     * 最近数据标示 （是：y,否：n）
     */
    private String isLatest;

    /**
     * 备注
     */
    private String remark;
    /**
     * 申请人
     */
    private String operator;
    /**
     * 申请时间
     */
    private Date operateTime;
    /**
     * 申请人类型
     * com.taobao.cun.auge.station.enums.OperatorTypeEnum;
     */
    private String operatorType;

    public String getTaobaoUserId() {
        return taobaoUserId;
    }

    public void setTaobaoUserId(String taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIncomeMode() {
        return incomeMode;
    }

    public void setIncomeMode(String incomeMode) {
        this.incomeMode = incomeMode;
    }

    public Date getIncomeModeBeginTime() {
        return incomeModeBeginTime;
    }

    public void setIncomeModeBeginTime(Date incomeModeBeginTime) {
        this.incomeModeBeginTime = incomeModeBeginTime;
    }

    public String getIsLatest() {
        return isLatest;
    }

    public void setIsLatest(String isLatest) {
        this.isLatest = isLatest;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }
}
