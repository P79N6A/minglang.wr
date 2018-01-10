package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class StationModeEnum implements Serializable {

    private static final Map<String, StationModeEnum> mappings = new HashMap<String, StationModeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;
    
    public static final StationModeEnum V4  = new StationModeEnum("v4", "4.0");
    public static final StationModeEnum V3  = new StationModeEnum("v3", "3.0");
    public static final StationModeEnum V2  = new StationModeEnum("v2", "2.0");
    public static final StationModeEnum V1  = new StationModeEnum("v1", "1.0");
    

    static {
    	mappings.put("V4", V4);
        mappings.put("V3", V3);
        mappings.put("V2", V2);
        mappings.put("V1", V1);
    }

    public StationModeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public StationModeEnum() {

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

    public static StationModeEnum valueof(String code) {
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
        StationModeEnum other = (StationModeEnum) obj;
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
