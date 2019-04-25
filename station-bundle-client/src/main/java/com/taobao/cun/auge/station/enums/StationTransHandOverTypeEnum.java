package com.taobao.cun.auge.station.enums;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StationTransHandOverTypeEnum implements Serializable {

    public static final StationTransHandOverTypeEnum STATION_TO_YOUPIN = new StationTransHandOverTypeEnum("STATION_TO_YOUPIN", "服务站转型天猫优品");
    public static final StationTransHandOverTypeEnum STATION_TO_YOUPIN_ELEC = new StationTransHandOverTypeEnum("STATION_TO_YOUPIN_ELEC", "服务站转型电器合作店");
    public static final StationTransHandOverTypeEnum STATION_TO_TPS_ELEC = new StationTransHandOverTypeEnum("STATION_TO_TPS_ELEC", "服务站转型电器体验店");
    public static final StationTransHandOverTypeEnum YOUPIN_TO_YOUPIN_ELEC = new StationTransHandOverTypeEnum("YOUPIN_TO_YOUPIN_ELEC", "天猫优品转型电器合作店");
    public static final StationTransHandOverTypeEnum YOUPIN_TO_TPS_ELEC = new StationTransHandOverTypeEnum("YOUPIN_TO_TPS_ELEC", "天猫优品转型电器体验店");
    public static final StationTransHandOverTypeEnum YOUPIN_ELEC_TO_TPS_ELEC = new StationTransHandOverTypeEnum("YOUPIN_ELEC_TO_TPS_ELEC", "电器合作店转型电器体验店");
    public static final StationTransHandOverTypeEnum REVENUE_HAND_OVER = new StationTransHandOverTypeEnum("REVENUE_HAND_OVER", "收入切换");


    private static final Map<String, StationTransHandOverTypeEnum> mappings = new HashMap<String, StationTransHandOverTypeEnum>();
    static {
        mappings.put("STATION_TO_YOUPIN", STATION_TO_YOUPIN);
        mappings.put("STATION_TO_YOUPIN_ELEC", STATION_TO_YOUPIN_ELEC);
        mappings.put("STATION_TO_TPS_ELEC", STATION_TO_TPS_ELEC);
        mappings.put("YOUPIN_TO_YOUPIN_ELEC", YOUPIN_TO_YOUPIN_ELEC);
        mappings.put("YOUPIN_TO_TPS_ELEC", YOUPIN_TO_TPS_ELEC);
        mappings.put("YOUPIN_ELEC_TO_TPS_ELEC", YOUPIN_ELEC_TO_TPS_ELEC);
        mappings.put("REVENUE_HAND_OVER", REVENUE_HAND_OVER);
    }
    @NotNull
    private String code;
    @NotNull
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof StationTransHandOverTypeEnum)) {
            return false;
        }
        StationTransHandOverTypeEnum objType = (StationTransHandOverTypeEnum) obj;
        return objType.getCode().equals(this.getCode());
    }

    public StationTransHandOverTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static StationTransHandOverTypeEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return mappings.get(code);
    }

    @SuppressWarnings("unused")
    private StationTransHandOverTypeEnum() {

    }


}
