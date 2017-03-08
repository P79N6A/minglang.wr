package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProtocolStateEnum implements Serializable {

    private static final long serialVersionUID = 4868749559601374950L;

    private static final Map<String, ProtocolStateEnum> mappings = new HashMap<String, ProtocolStateEnum>();

    private String code;
    private String desc;

    public static final ProtocolStateEnum VALID = new ProtocolStateEnum("VALID", "有效");
    public static final ProtocolStateEnum INVALID = new ProtocolStateEnum("INVALID", "无效");

    static {
        mappings.put("INVALID", INVALID);
        mappings.put("VALID", VALID);
    }

    public ProtocolStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ProtocolStateEnum() {

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

    public static ProtocolStateEnum valueof(String code) {
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
        ProtocolStateEnum other = (ProtocolStateEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}


