package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CuntaoCainiaoStationRelTypeEnum implements Serializable {

    private static final Map<String, CuntaoCainiaoStationRelTypeEnum> mappings = new HashMap<String, CuntaoCainiaoStationRelTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;

    public static final CuntaoCainiaoStationRelTypeEnum STATION = new CuntaoCainiaoStationRelTypeEnum("STATION", "服务站");
    public static final CuntaoCainiaoStationRelTypeEnum COUNTY_STATION = new CuntaoCainiaoStationRelTypeEnum("COUNTY_STATION", "县服务中心");


    static {
        mappings.put("STATION", STATION);
        mappings.put("COUNTY_STATION", COUNTY_STATION);
    }

    public CuntaoCainiaoStationRelTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public CuntaoCainiaoStationRelTypeEnum() {

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

    public static CuntaoCainiaoStationRelTypeEnum valueof(String code) {
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
        CuntaoCainiaoStationRelTypeEnum other = (CuntaoCainiaoStationRelTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
