package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class ProtocolGroupTypeEnum implements Serializable {

    private static final Map<String, ProtocolGroupTypeEnum> mappings = new HashMap<String, ProtocolGroupTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;
    
    public static final ProtocolGroupTypeEnum FIXED  = new ProtocolGroupTypeEnum("FIXED", "固点协议组");


    static {
    	mappings.put("FIXED", FIXED);
    }

    public ProtocolGroupTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ProtocolGroupTypeEnum() {

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

    public static ProtocolGroupTypeEnum valueof(String code) {
        if (code == null) {
            return null;
        }
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ProtocolGroupTypeEnum other = (ProtocolGroupTypeEnum) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }
}
