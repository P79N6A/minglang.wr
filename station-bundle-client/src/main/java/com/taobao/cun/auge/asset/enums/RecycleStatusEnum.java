package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiao on 17/5/17.
 */
public class RecycleStatusEnum implements Serializable{

    private static final long serialVersionUID = -4134780817945719435L;

    public static final RecycleStatusEnum Y = new RecycleStatusEnum("Y", "需回收");

    public static final RecycleStatusEnum N = new RecycleStatusEnum("N", "不回收");

    private static final Map<String, RecycleStatusEnum> mappings = new HashMap<String, RecycleStatusEnum>();

    static {
        mappings.put("Y", Y);
        mappings.put("N", N);
    }

    private String code;

    private String desc;

    public static RecycleStatusEnum valueOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return mappings.get(code);
    }

    public RecycleStatusEnum(String code, String desc) {
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
