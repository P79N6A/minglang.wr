package com.taobao.cun.auge.asset.enums;

/**
 * Created by xiao on 17/5/18.
 */
public enum AssetNotifyEnum {

    SIGN("SIGN", "签收");

    private String code;

    private String desc;

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

    AssetNotifyEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
