package com.taobao.cun.auge.station.enums;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haihu.fhh on 2016/5/4.
 */
public class TargetTypeEnum implements Serializable {

    private static final Map<String, TargetTypeEnum> mappings = new HashMap<String, TargetTypeEnum>();


    private static final long serialVersionUID = -3577617898765863359L;


    private String code;
    private String desc;

    //合伙人实例
    public static final TargetTypeEnum PARTNER_INSTANCE = new TargetTypeEnum("PARTNER_INSTANCE", "合伙人实例");
    public static final TargetTypeEnum PARTNER = new TargetTypeEnum("PARTNER", "合伙人");

    static {
        mappings.put("PARTNER_INSTANCE", PARTNER_INSTANCE);
        mappings.put("PARTNER", PARTNER);
    }

    public TargetTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public TargetTypeEnum() {

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

    public static TargetTypeEnum valueof(String code) {
        if (code == null)
            return null;
        return mappings.get(code);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TargetTypeEnum other = (TargetTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}