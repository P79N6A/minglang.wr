package com.taobao.cun.auge.fence.dto;

import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * Created by xiao on 18/6/17.
 */
public class FenceTemplateEditDto extends OperatorDto {

    private static final long serialVersionUID = 7077517442731365091L;

    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private String type;

    private String name;

    private String userType;

    private String limitCommodity;

    private String defaultCheck;

    private String commodityRule;

    private String rangeEnable;

    private String rangeRule;

    private String state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLimitCommodity() {
        return limitCommodity;
    }

    public void setLimitCommodity(String limitCommodity) {
        this.limitCommodity = limitCommodity;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
