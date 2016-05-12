package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProtocolTargetBizTypeEnum implements Serializable {
   
	private static final long serialVersionUID = 3718610373695202165L;

	private static final Map<String, ProtocolTargetBizTypeEnum> mappings = new HashMap<String, ProtocolTargetBizTypeEnum>();

    private String code;
    private String desc;

    /**关联业务类型 bizType or target type **/
    public static final ProtocolTargetBizTypeEnum CRIUS_STATION = new ProtocolTargetBizTypeEnum("CRIUS_STATION", "村点");
    public static final ProtocolTargetBizTypeEnum PARTNER = new ProtocolTargetBizTypeEnum("PARTNER", "合伙人");
    public static final ProtocolTargetBizTypeEnum PARTNER_INSTANCE = new ProtocolTargetBizTypeEnum("PARTNER_INSTANCE", "合伙实例");


    static {
        mappings.put("CRIUS_STATION", CRIUS_STATION);
        mappings.put("PARTNER", PARTNER);
        mappings.put("PARTNER_INSTANCE", PARTNER_INSTANCE);
    }

    public ProtocolTargetBizTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ProtocolTargetBizTypeEnum() {

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

    public static ProtocolTargetBizTypeEnum valueof(String code) {
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
        ProtocolTargetBizTypeEnum other = (ProtocolTargetBizTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}

