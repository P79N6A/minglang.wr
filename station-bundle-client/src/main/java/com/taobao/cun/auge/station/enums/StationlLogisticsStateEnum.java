package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务站表中物流状态枚举
 * @author quanzhu.wangqz
 *
 */
public class StationlLogisticsStateEnum implements Serializable {

    private static final long serialVersionUID = -1197977282708260567L;

    // 暂存
    public static final StationlLogisticsStateEnum ARRIVE = new StationlLogisticsStateEnum("ARRIVE", "能到");

    // 新
    public static final StationlLogisticsStateEnum NOT_ARRIVE = new StationlLogisticsStateEnum("NOT_ARRIVE", "不能到");

    private static final Map<String, StationlLogisticsStateEnum> mappings = new HashMap<String, StationlLogisticsStateEnum>();

    static {
        mappings.put("ARRIVE", ARRIVE);
        mappings.put("NOT_ARRIVE", NOT_ARRIVE);
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
        if (!(obj instanceof StationlLogisticsStateEnum))
            return false;
        StationlLogisticsStateEnum objType = (StationlLogisticsStateEnum) obj;
        return objType.getCode().equals(this.getCode());
    }

    public StationlLogisticsStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static StationlLogisticsStateEnum valueof(String code) {
        if (code == null)
            return null;
        return mappings.get(code);
    }

    @SuppressWarnings("unused")
    private StationlLogisticsStateEnum() {

    }
}
