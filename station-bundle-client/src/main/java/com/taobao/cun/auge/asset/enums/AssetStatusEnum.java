package com.taobao.cun.auge.asset.enums;

/**
 * Created by xiao on 17/5/17.
 */
public enum  AssetStatusEnum {

    USE("USE", "使用中"), RECYCLE("RECYCLE", "待回收"),TRANSFER("TRANSFER", "转移中"),DISTRIBUTE("distribute", "分发中"),SCRAP("SCRAP", "已报废");

    private String code;

    private String desc;

    AssetStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
