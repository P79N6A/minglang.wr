package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StationCategoryEnum implements Serializable {

    private static final Map<String, StationCategoryEnum> mappings = new HashMap<String, StationCategoryEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;

    //private String[] categories = {"综合","农资","家电","3C数码","汽摩配","家装","服装","母婴","日百","餐饮","其他"};
    public static final StationCategoryEnum ZONGHE = new StationCategoryEnum("ZONGHE", "综合");
    public static final StationCategoryEnum NONGZI = new StationCategoryEnum("NONGZI", "农资");
    public static final StationCategoryEnum JIADIAN = new StationCategoryEnum("JIADIAN", "家电");
    public static final StationCategoryEnum SANCSHUMA = new StationCategoryEnum("SANCSHUMA", "3C数码");
    public static final StationCategoryEnum QIMOPEI = new StationCategoryEnum("QIMOPEI", "汽摩配");
    public static final StationCategoryEnum JIAZHUANG = new StationCategoryEnum("JIAZHUANG", "家装");
    public static final StationCategoryEnum FUZHUANG = new StationCategoryEnum("FUZHUANG", "服装");
    public static final StationCategoryEnum MUYING = new StationCategoryEnum("MUYING", "母婴");
    public static final StationCategoryEnum RIBAI = new StationCategoryEnum("RIBAI", "日百");
    public static final StationCategoryEnum CANYIN = new StationCategoryEnum("CANYIN", "餐饮");
    public static final StationCategoryEnum QITA = new StationCategoryEnum("QITA", "其他");


    static {
        mappings.put("ZONGHE", ZONGHE);
        mappings.put("NONGZI", NONGZI);
        mappings.put("JIADIAN", JIADIAN);
        mappings.put("SANCSHUMA", SANCSHUMA);
        mappings.put("QIMOPEI", QIMOPEI);
        mappings.put("JIAZHUANG", JIAZHUANG);
        mappings.put("FUZHUANG", FUZHUANG);
        mappings.put("MUYING", MUYING);
        mappings.put("RIBAI", RIBAI);
        mappings.put("CANYIN", CANYIN);
        mappings.put("QITA", QITA);
    }

    public StationCategoryEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public StationCategoryEnum() {

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

    public static StationCategoryEnum valueof(String code) {
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
        StationCategoryEnum other = (StationCategoryEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}