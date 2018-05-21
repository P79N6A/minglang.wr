package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StationFeatureOpModeEnum implements Serializable{

    private static final long serialVersionUID = 3906778131591078106L;

    private static final Map<String, StationFeatureOpModeEnum> mappings = new HashMap<String, StationFeatureOpModeEnum>();

    private String code;
    private String desc;
   
    public static final StationFeatureOpModeEnum Y  = new StationFeatureOpModeEnum("Y", "y");
    public static final StationFeatureOpModeEnum N = new StationFeatureOpModeEnum("N", "n");


    static {
        mappings.put("Y", Y);
        mappings.put("N", N);
    }

    public StationFeatureOpModeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public StationFeatureOpModeEnum() {

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

    public static StationFeatureOpModeEnum valueof(String code) {
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
        StationFeatureOpModeEnum other = (StationFeatureOpModeEnum) obj;
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
