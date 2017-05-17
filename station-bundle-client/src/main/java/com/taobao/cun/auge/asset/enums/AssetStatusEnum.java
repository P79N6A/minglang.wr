package com.taobao.cun.auge.asset.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiao on 17/5/17.
 */
public enum  AssetStatusEnum {

    USE("USE", "使用中"), RECYCLE("RECYCLE", "待回收"),TRANSFER("TRANSFER", "转移中"),DISTRIBUTE("DISTRIBUTE", "分发中"),SCRAP("SCRAP", "已报废");

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

    public static List<String> getValidStatusList() {
        List<String> list = new ArrayList<String>();
        list.add(USE.getCode());
        list.add(RECYCLE.getCode());
        list.add(TRANSFER.getCode());
        list.add(DISTRIBUTE.getCode());
        return list;
    }
}
