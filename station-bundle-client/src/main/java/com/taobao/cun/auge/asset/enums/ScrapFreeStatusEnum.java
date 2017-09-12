package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiao on 17/5/17.
 */
public class ScrapFreeStatusEnum implements Serializable{

    private static final long serialVersionUID = 5592784460494999255L;

    public static final ScrapFreeStatusEnum Y = new ScrapFreeStatusEnum("Y", "免赔");

    public static final ScrapFreeStatusEnum N = new ScrapFreeStatusEnum("N", "非免赔");

    private static final Map<String, ScrapFreeStatusEnum> mappings = new HashMap<String, ScrapFreeStatusEnum>();


    static {
        mappings.put("Y", Y);
        mappings.put("N", N);
    }

    private String code;

    private String desc;

    public static ScrapFreeStatusEnum valueOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return mappings.get(code);
    }

    public ScrapFreeStatusEnum(String code, String desc) {
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
