package com.taobao.cun.auge.fence.dto;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.fence.enums.FenceTypeEnum;

/**
 * Created by xiao on 18/6/15.
 */
public class FenceTemplateListDto implements Serializable {

    private static final long serialVersionUID = 4684664316899136843L;

    private Long id;

    private String name;

    private FenceTypeEnum typeEnum;

    private String userType;

    private String limitCommodity;

    private String modifier;

    private String modifierName;

    private Date gmtModified;

    private String status;

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

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
