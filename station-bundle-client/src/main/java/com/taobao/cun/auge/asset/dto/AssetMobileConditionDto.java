package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

/**
 * Created by xiao on 17/5/25.
 */
public class AssetMobileConditionDto implements Serializable{

    private static final long serialVersionUID = -4651552156524712060L;

    private String name;

    private String code;

    public AssetMobileConditionDto() {
    }

    public AssetMobileConditionDto(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
