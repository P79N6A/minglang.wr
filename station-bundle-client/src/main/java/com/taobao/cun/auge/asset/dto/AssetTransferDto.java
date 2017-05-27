package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * Created by xiao on 17/5/22.
 */
public class AssetTransferDto extends OperatorDto implements Serializable{

    private static final long serialVersionUID = 8306426246791798460L;

    private String receiverWorkNo;

    private String reason;

    private String receiverAreaId;

    //物流费用
    private String payment;

    //物流距离
    private String distance;

    //不转移的资产id列表 转移到本县使用
    private List<Long> unTransferAssetIdList;

    //要转移的资产id列表 转移到他县使用
    private List<Long> transferAssetIdList;

    public String getReceiverWorkNo() {
        return receiverWorkNo;
    }

    public void setReceiverWorkNo(String receiverWorkNo) {
        this.receiverWorkNo = receiverWorkNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReceiverAreaId() {
        return receiverAreaId;
    }

    public void setReceiverAreaId(String receiverAreaId) {
        this.receiverAreaId = receiverAreaId;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<Long> getUnTransferAssetIdList() {
        return unTransferAssetIdList;
    }

    public void setUnTransferAssetIdList(List<Long> unTransferAssetIdList) {
        this.unTransferAssetIdList = unTransferAssetIdList;
    }

    public List<Long> getTransferAssetIdList() {
        return transferAssetIdList;
    }

    public void setTransferAssetIdList(List<Long> transferAssetIdList) {
        this.transferAssetIdList = transferAssetIdList;
    }
}
