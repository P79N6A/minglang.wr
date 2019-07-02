package com.taobao.cun.auge.contactrecord.dto;

/**
 * 政府拜访风险信息
 *
 * @author chengyu.zhoucy
 */
public class CuntaoGovContactRecordRiskDto {
    /**
     * 政府拜访记录
     */
    private Long contactId;

    /**
     * 风险等级
     */
    private String riskLevel;

    /**
     * 问题类型
     */
    private String riskType;

    /**
     * 问题类型描述
     */
    private String riskDesc;

    /**
     * 状态
     */
    private String state;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public String getRiskDesc() {
        return riskDesc;
    }

    public void setRiskDesc(String riskDesc) {
        this.riskDesc = riskDesc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
