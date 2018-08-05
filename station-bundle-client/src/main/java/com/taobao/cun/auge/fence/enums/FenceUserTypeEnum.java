package com.taobao.cun.auge.fence.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiao on 18/6/15.
 */
public class FenceUserTypeEnum implements Serializable{


    private static final long serialVersionUID = 5525199073000726953L;

    static final FenceUserTypeEnum ALL = new FenceUserTypeEnum("ALL", "不限用户");
    static final FenceUserTypeEnum BIND = new FenceUserTypeEnum("BIND", "绑定用户");
    private static final Map<String, FenceUserTypeEnum> mappings = new HashMap<String, FenceUserTypeEnum>();

    static {
        mappings.put("ALL", ALL);
        mappings.put("BIND", BIND);
    }

    private String code;

    private String desc;

    public static FenceUserTypeEnum valueOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return mappings.get(code);
    }

    public FenceUserTypeEnum(String code, String desc) {
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
