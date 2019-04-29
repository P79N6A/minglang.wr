package com.taobao.cun.auge.station.enums;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StationTransHandOverNodeEnum implements Serializable {

    public static final StationTransHandOverNodeEnum WAIT_TRANS = new StationTransHandOverNodeEnum("WAIT_TRANS", "等待签署协议");
    public static final StationTransHandOverNodeEnum WAIT_FREZON = new StationTransHandOverNodeEnum("WAIT_FREZON", "等待补缴保证金");
    public static final StationTransHandOverNodeEnum WAIT_DECOTATION = new StationTransHandOverNodeEnum("WAIT_DECOTATION", "等待装修");
    public static final StationTransHandOverNodeEnum WAIT_OPEN = new StationTransHandOverNodeEnum("WAIT_OPEN", "等待开业");
    public static final StationTransHandOverNodeEnum FINISH = new StationTransHandOverNodeEnum("FINISH", "完结");


    private static final Map<String, StationTransHandOverNodeEnum> mappings = new HashMap<String, StationTransHandOverNodeEnum>();
    static {
        mappings.put("WAIT_TRANS", WAIT_TRANS);
        mappings.put("WAIT_FREZON", WAIT_FREZON);
        mappings.put("WAIT_DECOTATION", WAIT_DECOTATION);
        mappings.put("WAIT_OPEN", WAIT_OPEN);
        mappings.put("FINISH", FINISH);
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
        if (!(obj instanceof StationTransHandOverNodeEnum)) {
            return false;
        }
        StationTransHandOverNodeEnum objType = (StationTransHandOverNodeEnum) obj;
        return objType.getCode().equals(this.getCode());
    }

    public StationTransHandOverNodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static StationTransHandOverNodeEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return mappings.get(code);
    }

    @SuppressWarnings("unused")
    private StationTransHandOverNodeEnum() {

    }


}
