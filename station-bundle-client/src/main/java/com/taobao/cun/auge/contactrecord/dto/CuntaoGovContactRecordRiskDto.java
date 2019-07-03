package com.taobao.cun.auge.contactrecord.dto;

import com.taobao.cun.auge.contactrecord.enums.CuntaoGovContactRiskStateEnum;
import com.taobao.cun.auge.contactrecord.enums.CuntaoGovContactRiskLevelEnum;
import com.taobao.cun.auge.contactrecord.enums.CuntaoGovContactRiskTypeEnum;

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
    private CuntaoGovContactRiskLevelEnum riskLevel;

    /**
     * 问题类型
     */
    private CuntaoGovContactRiskTypeEnum riskType;

    /**
     * 问题类型描述
     */
    private String riskDesc;

    /**
     * 状态
     */
    private CuntaoGovContactRiskStateEnum state;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getRiskDesc() {
        return riskDesc;
    }

    public void setRiskDesc(String riskDesc) {
        this.riskDesc = riskDesc;
    }

    public CuntaoGovContactRiskLevelEnum getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(CuntaoGovContactRiskLevelEnum riskLevel) {
        this.riskLevel = riskLevel;
    }

    public CuntaoGovContactRiskTypeEnum getRiskType() {
        return riskType;
    }

    public void setRiskType(CuntaoGovContactRiskTypeEnum riskType) {
        this.riskType = riskType;
    }

    public CuntaoGovContactRiskStateEnum getState() {
        return state;
    }

    public void setState(CuntaoGovContactRiskStateEnum state) {
        this.state = state;
    }
}
