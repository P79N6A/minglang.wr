package com.taobao.cun.auge.contactrecord.dto;

import java.util.Date;

/**
 * 政府拜访记录摘要
 */
public class CuntaoGovContactRecordSummaryDto {
    private Long id;
    /**
     * 县服务中心ID
     */
    private Long countyId;
    /**
     * 拜访人工号
     */
    private String visitorWorkId;
    /**
     * 拜访人姓名
     */
    private String visitorName;
    /**
     * 拜访时间
     */
    private Date contactDate;

    /**
     * 高风险数
     */
    private Integer highRiskNum;

    /**
     * 中风险数
     */
    private Integer middleRiskNum;

    /**
     * 低风险数
     */
    private Integer lowRiskNum;
    /**
     * 拜访记录描述
     */
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public String getVisitorWorkId() {
        return visitorWorkId;
    }

    public void setVisitorWorkId(String visitorWorkId) {
        this.visitorWorkId = visitorWorkId;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public Integer getHighRiskNum() {
        return highRiskNum;
    }

    public void setHighRiskNum(Integer highRiskNum) {
        this.highRiskNum = highRiskNum;
    }

    public Integer getMiddleRiskNum() {
        return middleRiskNum;
    }

    public void setMiddleRiskNum(Integer middleRiskNum) {
        this.middleRiskNum = middleRiskNum;
    }

    public Integer getLowRiskNum() {
        return lowRiskNum;
    }

    public void setLowRiskNum(Integer lowRiskNum) {
        this.lowRiskNum = lowRiskNum;
    }
}
