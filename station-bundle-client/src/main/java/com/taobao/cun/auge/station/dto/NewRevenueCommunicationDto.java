package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.common.OperatorDto;

import java.io.Serializable;
import java.util.Date;

public class NewRevenueCommunicationDto extends OperatorDto implements Serializable {

    private Long id;

    private String objectId;

    private String businessCode;

    private String status;

    private Date commuTime;

    private String commuAddress;

    private String commuContent;

    private String remark;

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

    public Date getCommuTime() {
        return commuTime;
    }

    public void setCommuTime(Date commuTime) {
        this.commuTime = commuTime;
    }

    public String getCommuAddress() {
        return commuAddress;
    }

    public void setCommuAddress(String commuAddress) {
        this.commuAddress = commuAddress;
    }

    public String getCommuContent() {
        return commuContent;
    }

    public void setCommuContent(String commuContent) {
        this.commuContent = commuContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
