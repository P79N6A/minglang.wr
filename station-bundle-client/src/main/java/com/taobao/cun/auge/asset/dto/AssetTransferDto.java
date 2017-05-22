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

    private List<Long> unTransferAssetIdList;

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

    public List<Long> getUnTransferAssetIdList() {
        return unTransferAssetIdList;
    }

    public void setUnTransferAssetIdList(List<Long> unTransferAssetIdList) {
        this.unTransferAssetIdList = unTransferAssetIdList;
    }
}
