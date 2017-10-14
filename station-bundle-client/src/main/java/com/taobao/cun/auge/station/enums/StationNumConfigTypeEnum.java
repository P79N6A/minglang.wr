package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 村点编号配置表类型字段枚举
 * @author quanzhu.wangqz
 *
 */
public class StationNumConfigTypeEnum implements Serializable {

    private static final Map<String, StationNumConfigTypeEnum> mappings = new HashMap<String, StationNumConfigTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;
   
    public static final StationNumConfigTypeEnum C  = new StationNumConfigTypeEnum("C", "村");
    public static final StationNumConfigTypeEnum D = new StationNumConfigTypeEnum("D", "电器");
    public static final StationNumConfigTypeEnum M = new StationNumConfigTypeEnum("M", "母婴");


    static {
    	mappings.put("C", C);
        mappings.put("D", D);
        mappings.put("M", M);
    }

    public StationNumConfigTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public StationNumConfigTypeEnum() {

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

    public static StationNumConfigTypeEnum valueof(String code) {
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
        StationNumConfigTypeEnum other = (StationNumConfigTypeEnum) obj;
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
