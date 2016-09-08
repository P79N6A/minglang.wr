package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务站表 场地固点类型 枚举
 * @author quanzhu.wangqz
 *
 */
public class StationFixedTypeEnum implements Serializable {

    private static final Map<String, StationFixedTypeEnum> mappings = new HashMap<String, StationFixedTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;

    public static final StationFixedTypeEnum GOV_FIXED = new StationFixedTypeEnum(
			"GOV_FIXED", "政府固点");
	
	public static final StationFixedTypeEnum TRIPARTITE_FIXED = new StationFixedTypeEnum(
			"TRIPARTITE_FIXED", "三方租赁");


    static {
        mappings.put("GOV_FIXED", GOV_FIXED);
        mappings.put("TRIPARTITE_FIXED", TRIPARTITE_FIXED);
    }

    public StationFixedTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public StationFixedTypeEnum() {

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

    public static StationFixedTypeEnum valueof(String code) {
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
        StationFixedTypeEnum other = (StationFixedTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
