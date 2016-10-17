package com.taobao.cun.auge.station.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiao on 16/10/17.
 */
public class WisdomCountyStateEnum {


    public static final WisdomCountyStateEnum UNAPPLY = new WisdomCountyStateEnum(
            "UNAPPLY", "未报名");
    public static final WisdomCountyStateEnum APPLY = new WisdomCountyStateEnum(
            "APPLY", "报名");
    public static final WisdomCountyStateEnum AUDIT_PASS = new WisdomCountyStateEnum(
            "AUDIT_PASS", "审核通过");
    public static final WisdomCountyStateEnum AUDIT_FAIL = new WisdomCountyStateEnum(
            "AUDIT_FAIL", "审核拒绝");

    public static final Map<String, WisdomCountyStateEnum> mappings = new HashMap<String, WisdomCountyStateEnum>();

    static {
        mappings.put("UNAPPLY", UNAPPLY);
        mappings.put("APPLY", APPLY);
        mappings.put("AUDIT_PASS", AUDIT_PASS);
        mappings.put("AUDIT_FAIL", AUDIT_FAIL);
    }

    private String code;
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
        if (obj == null)
            return false;
        if (!(obj instanceof StationStateEnum))
            return false;
        StationStateEnum objType = (StationStateEnum) obj;
        return objType.getCode().equals(this.getCode());
    }

    public WisdomCountyStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WisdomCountyStateEnum valueof(String code) {
        if (code == null)
            return null;
        return mappings.get(code);
    }

    @SuppressWarnings("unused")
    private WisdomCountyStateEnum() {

    }

}
