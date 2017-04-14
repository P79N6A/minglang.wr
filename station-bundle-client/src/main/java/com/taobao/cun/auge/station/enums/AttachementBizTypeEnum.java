package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 附件表  业务类型枚举
 * @author quanzhu.wangqz
 *
 */
public class AttachementBizTypeEnum implements Serializable {

    private static final Map<String, AttachementBizTypeEnum> mappings = new HashMap<String, AttachementBizTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;

    public static final AttachementBizTypeEnum PARTNER = new AttachementBizTypeEnum("PARTNER", "合伙人");
    public static final AttachementBizTypeEnum CRIUS_STATION = new AttachementBizTypeEnum("CRIUS_STATION", "村点");
    public static final AttachementBizTypeEnum STATION_DECORATE = new AttachementBizTypeEnum("STATION_DECORATE", "村点装修记录");
    public static final AttachementBizTypeEnum WISDOM_COUNTY_APPLY = new AttachementBizTypeEnum("WISDOM_COUNTY_APPLY", "智慧县域报名附件");
    public static final AttachementBizTypeEnum CUNGOV_CONSULT = new AttachementBizTypeEnum("CUNGOV_CONSULT", "举报投诉附件");
    public static final AttachementBizTypeEnum INCENTIVE_PROGRAM = new AttachementBizTypeEnum("INCENTIVE_PROGRAM", "激励方案附件");
    public static final AttachementBizTypeEnum COUNTY_STATION = new AttachementBizTypeEnum("county_station", "县点");


    static {
        mappings.put("PARTNER", PARTNER);
        mappings.put("CRIUS_STATION", CRIUS_STATION);
        mappings.put("STATION_DECORATE", STATION_DECORATE);
        mappings.put("WISDOM_COUNTY_APPLY", WISDOM_COUNTY_APPLY);
        mappings.put("CUNGOV_CONSULT", CUNGOV_CONSULT);
        mappings.put("INCENTIVE_PROGRAM", INCENTIVE_PROGRAM);
        mappings.put("INCENTIVE_PROGRAM", COUNTY_STATION);

    }

    public AttachementBizTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AttachementBizTypeEnum() {

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

    public static AttachementBizTypeEnum valueof(String code) {
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
        AttachementBizTypeEnum other = (AttachementBizTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}