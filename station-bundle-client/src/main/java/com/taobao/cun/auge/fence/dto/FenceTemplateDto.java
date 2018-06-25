package com.taobao.cun.auge.fence.dto;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.fence.enums.FenceTypeEnum;
import com.taobao.cun.auge.fence.enums.FenceUserTypeEnum;

/**
 * Created by xiao on 18/6/22.
 */
public class FenceTemplateDto implements Serializable {

    private static final long serialVersionUID = 8003436953469484384L;

    private Long id;

    private String name;

    private FenceTypeEnum typeEnum;

    private FenceUserTypeEnum userTypeEnum;

    private String limitCommodity;

    private Date gmtModified;

    private Date gmtCreate;

    private String state;

    private String creator;

    private String modifier;

    private String defaultCheck;

    private String commodityRule;

    private String rangeEnable;

    private String rangeRule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FenceTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(FenceTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public FenceUserTypeEnum getUserTypeEnum() {
        return userTypeEnum;
    }

    public void setUserTypeEnum(FenceUserTypeEnum userTypeEnum) {
        this.userTypeEnum = userTypeEnum;
    }

    public String getLimitCommodity() {
        return limitCommodity;
    }

    public void setLimitCommodity(String limitCommodity) {
        this.limitCommodity = limitCommodity;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getDefaultCheck() {
        return defaultCheck;
    }

    public void setDefaultCheck(String defaultCheck) {
        this.defaultCheck = defaultCheck;
    }

    public String getCommodityRule() {
        return commodityRule;
    }

    public void setCommodityRule(String commodityRule) {
        this.commodityRule = commodityRule;
    }

    public String getRangeEnable() {
        return rangeEnable;
    }

    public void setRangeEnable(String rangeEnable) {
        this.rangeEnable = rangeEnable;
    }

    public String getRangeRule() {
        return rangeRule;
    }

    public void setRangeRule(String rangeRule) {
        this.rangeRule = rangeRule;
    }
}
