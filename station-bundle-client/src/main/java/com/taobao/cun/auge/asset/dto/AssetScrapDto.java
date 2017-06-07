package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * Created by xiao on 17/6/7.
 */
public class AssetScrapDto extends AssetOperatorDto implements Serializable {

    private static final long serialVersionUID = -2923233237966682489L;

    private String reason;

    private String free;

    private Long scrapAreaId;

    private String scrapAreaType;

    private String payment;

    private List<Long> scrapAssetIdList;

    private List<String> attachmentList;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public Long getScrapAreaId() {
        return scrapAreaId;
    }

    public void setScrapAreaId(Long scrapAreaId) {
        this.scrapAreaId = scrapAreaId;
    }

    public String getScrapAreaType() {
        return scrapAreaType;
    }

    public void setScrapAreaType(String scrapAreaType) {
        this.scrapAreaType = scrapAreaType;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<Long> getScrapAssetIdList() {
        return scrapAssetIdList;
    }

    public void setScrapAssetIdList(List<Long> scrapAssetIdList) {
        this.scrapAssetIdList = scrapAssetIdList;
    }

    public List<String> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<String> attachmentList) {
        this.attachmentList = attachmentList;
    }

}
