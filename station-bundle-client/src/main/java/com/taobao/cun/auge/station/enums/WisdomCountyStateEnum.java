package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiao on 16/10/17.
 */
public class WisdomCountyStateEnum implements Serializable{

    private static final long serialVersionUID = 4983117822794087359L;

    public static final WisdomCountyStateEnum UNAPPLY = new WisdomCountyStateEnum(
            "UNAPPLY", "未报名");
    public static final WisdomCountyStateEnum APPLY = new WisdomCountyStateEnum(
            "APPLY", "已报名");
    public static final WisdomCountyStateEnum AUDIT_PASS = new WisdomCountyStateEnum(
            "AUDIT_PASS", "审核通过");
    public static final WisdomCountyStateEnum AUDIT_FAIL = new WisdomCountyStateEnum(
            "AUDIT_FAIL", "审核拒绝");

    public static final Map<String, WisdomCountyStateEnum> mappings = new HashMap<String, WisdomCountyStateEnum>();

    static {
        mappings.put("UNAPPLY", UNAPPLY);
        mappings.put("APPLY", APPLY);
        mappings.put("AUDIT_FAIL", AUDIT_FAIL);
        mappings.put("AUDIT_PASS", AUDIT_PASS);
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
        if (!(obj instanceof WisdomCountyStateEnum))
            return false;
        WisdomCountyStateEnum objType = (WisdomCountyStateEnum) obj;
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

    public static List<WisdomCountyStateEnum> enums(){
        List<WisdomCountyStateEnum> enums = new ArrayList<WisdomCountyStateEnum>();
        enums.add(UNAPPLY);
        enums.add(APPLY);
        enums.add(AUDIT_PASS);
        enums.add(AUDIT_FAIL);
        return enums;
    }

    @SuppressWarnings("unused")
    private WisdomCountyStateEnum() {

    }

}
