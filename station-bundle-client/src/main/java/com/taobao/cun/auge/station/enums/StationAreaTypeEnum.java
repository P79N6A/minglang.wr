package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class StationAreaTypeEnum implements Serializable {

    private static final Map<String, StationAreaTypeEnum> mappings = new HashMap<String, StationAreaTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;
   
    public static final StationAreaTypeEnum FREE  = new StationAreaTypeEnum("FREE", "不固点");
    public static final StationAreaTypeEnum FIX = new StationAreaTypeEnum("FIX", "固点");
    public static final StationAreaTypeEnum FIX_NEW = new StationAreaTypeEnum("FIX_NEW", "固点");


    static {
    	mappings.put("FREE", FREE);
        mappings.put("FIX", FIX);
        mappings.put("FIX_NEW", FIX_NEW);
    }

    public StationAreaTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public StationAreaTypeEnum() {

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

    public static StationAreaTypeEnum valueof(String code) {
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
        StationAreaTypeEnum other = (StationAreaTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
