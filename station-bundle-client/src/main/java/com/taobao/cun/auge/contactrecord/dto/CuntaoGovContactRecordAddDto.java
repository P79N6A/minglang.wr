package com.taobao.cun.auge.contactrecord.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 添加拜访记录
 *
 * @author chengyu.zhoucy
 */
public class CuntaoGovContactRecordAddDto {
    @NotNull(message = "县服务中心ID不能为空")
    private Long countyId;
    @NotBlank(message="操作人不能为空")
    private String operator;
    @NotNull(message = "拜访日期不能为空")
    private Date contactDate;
    @NotBlank(message = "拜访方式不能为空")
    private String contactWay;
    @NotBlank(message = "拜访对象姓名不能为空")
    private String contactorName;
    @NotBlank(message = "拜访对象联系电话不能为空")
    private String contactorPhone;
    @NotBlank(message = "拜访对象部门不能为空")
    private String contactorDept;
    @NotBlank(message = "拜访对象职务不能为空")
    private String contactorTitle;
    @NotBlank(message = "拜访事由不能为空")
    private String reason;
    @NotBlank(message = "拜访记录描述不能为空")
    private String content;
    /**
     * 拜访风险信息（JSON格式）
     * [
     * {"riskLevel":"hight","riskType":"xxxx","riskDesc":"xxxx","state":"resolved"},
     * {"riskLevel":"low","riskType":"xxxx","riskDesc":"xxxx","state":"resolved"}
     * ]
     */
    private String riskInfos;

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
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

    public String getRiskInfos() {
        return riskInfos;
    }

    public void setRiskInfos(String riskInfos) {
        this.riskInfos = riskInfos;
    }
}
