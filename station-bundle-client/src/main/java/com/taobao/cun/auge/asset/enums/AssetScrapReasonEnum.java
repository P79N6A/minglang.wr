package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetScrapReasonEnum implements Serializable{

    private static final long serialVersionUID = 7462321301856747708L;

    public static final AssetScrapReasonEnum STEAL = new AssetScrapReasonEnum("STEAL", "资产被盗");
    public static final AssetScrapReasonEnum NATURE = new AssetScrapReasonEnum("NATURE", "自然灾害");
    public static final AssetScrapReasonEnum LOST = new AssetScrapReasonEnum("LOST", "资产丢失");
    public static final AssetScrapReasonEnum DESTROY = new AssetScrapReasonEnum("DESTROY", "资产损毁");
    public static final AssetScrapReasonEnum OTHER = new AssetScrapReasonEnum("OTHER", "其他原因");


    private static final Map<String, AssetScrapReasonEnum> mappings = new HashMap<String, AssetScrapReasonEnum>();


    static {
        mappings.put("STEAL", STEAL);
        mappings.put("NATURE", NATURE);
        mappings.put("LOST", LOST);
        mappings.put("DESTROY", DESTROY);
        mappings.put("OTHER", OTHER);
    }

    private String code;

    private String desc;

    public static AssetScrapReasonEnum valueOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return mappings.get(code);
    }

    public AssetScrapReasonEnum(String code, String desc) {
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
