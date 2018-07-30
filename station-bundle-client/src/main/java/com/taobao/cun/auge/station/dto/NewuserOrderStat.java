package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class NewuserOrderStat implements Serializable {
    private Long adzoneId;
    /**
     * 小二会员id
     */
    private Long taobaoUserId;
    /**
     * 统计月份
     */
    private String statDate;
    /**
     * 注册人数
     */
    private Long registerCnt;
    /**
     * 激活人数
     */
    private Long activateCnt;
    /**
     * 首购人数
     */
    private Long buyCnt;
    /**
     * 确认收货人数
     */
    private Long confirmCnt;
    /**
     * 绑卡人数
     */
    private Long carBindCnt;


    public NewuserOrderStat() {
        registerCnt = 0L;
        activateCnt = 0L;
        buyCnt = 0L;
        confirmCnt = 0L;
        carBindCnt = 0L;
    }

    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public Long getRegisterCnt() {
        return registerCnt;
    }

    public void setRegisterCnt(Long registerCnt) {
        this.registerCnt = registerCnt;
    }

    public Long getActivateCnt() {
        return activateCnt;
    }

    public void setActivateCnt(Long activateCnt) {
        this.activateCnt = activateCnt;
    }

    public Long getBuyCnt() {
        return buyCnt;
    }

    public void setBuyCnt(Long buyCnt) {
        this.buyCnt = buyCnt;
    }

    public Long getConfirmCnt() {
        return confirmCnt;
    }

    public void setConfirmCnt(Long confirmCnt) {
        this.confirmCnt = confirmCnt;
    }

    public Long getCarBindCnt() {
        return carBindCnt;
    }

    public void setCarBindCnt(Long carBindCnt) {
        this.carBindCnt = carBindCnt;
    }

    public Long getAdzoneId() {
        return adzoneId;
    }

    public void setAdzoneId(Long adzoneId) {
        this.adzoneId = adzoneId;
    }
}
