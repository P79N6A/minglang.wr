package com.taobao.cun.auge.station.condition;

import com.taobao.cun.auge.common.OperatorDto;

import java.io.Serializable;

public class NewRevenueCommunicationCondition extends OperatorDto implements Serializable {

    private Long id;

    private String objectId;

    private String businessCode;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
