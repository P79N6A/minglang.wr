package com.taobao.cun.auge.contactrecord.dto;

import com.taobao.cun.auge.contactrecord.enums.CuntaoGovContactRecordWayEnum;

import java.util.Date;
import java.util.List;

/**
 * 政府拜访记录详情
 */
public class CuntaoGovContactRecordDetailDto {
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
     * 拜访日期
     */
    private Date contactDate;
    /**
     * 拜访方式
     */
    private CuntaoGovContactRecordWayEnum contactWayEnum;
    /**
     * 拜访对象姓名
     */
    private String contactorName;
    /**
     * 拜访对象联系电话
     */
    private String contactorPhone;
    /**
     * 拜访对象部门
     */
    private String contactorDept;
    /**
     * 拜访对象职务
     */
    private String contactorTitle;
    /**
     * 拜访事由
     */
    private String reason;
    /**
     * 拜访记录描述
     */
    private String content;

    /**
     * 风险信息
     */
    private List<CuntaoGovContactRecordRiskDto> risks;

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

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public CuntaoGovContactRecordWayEnum getContactWayEnum() {
        return contactWayEnum;
    }

    public void setContactWayEnum(CuntaoGovContactRecordWayEnum contactWayEnum) {
        this.contactWayEnum = contactWayEnum;
    }

    public String getContactorName() {
        return contactorName;
    }

    public void setContactorName(String contactorName) {
        this.contactorName = contactorName;
    }

    public String getContactorPhone() {
        return contactorPhone;
    }

    public void setContactorPhone(String contactorPhone) {
        this.contactorPhone = contactorPhone;
    }

    public String getContactorDept() {
        return contactorDept;
    }

    public void setContactorDept(String contactorDept) {
        this.contactorDept = contactorDept;
    }

    public String getContactorTitle() {
        return contactorTitle;
    }

    public void setContactorTitle(String contactorTitle) {
        this.contactorTitle = contactorTitle;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CuntaoGovContactRecordRiskDto> getRisks() {
        return risks;
    }

    public void setRisks(List<CuntaoGovContactRecordRiskDto> risks) {
        this.risks = risks;
    }
}
