package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProtocolTypeEnum implements Serializable {
 
	private static final long serialVersionUID = -3778546477529242130L;

	private static final Map<String, ProtocolTypeEnum> mappings = new HashMap<String, ProtocolTypeEnum>();

    private String code;
    private String desc;

    public static final ProtocolTypeEnum SETTLE_PRO = new ProtocolTypeEnum("SETTLE_PRO", "入驻协议");

    public static final ProtocolTypeEnum MANAGE_PRO = new ProtocolTypeEnum("MANAGE_PRO", "管理协议");

    public static final ProtocolTypeEnum PARTNER_QUIT_PRO = new ProtocolTypeEnum("PARTNER_QUIT_PRO", "合伙人退出协议");

    public static final ProtocolTypeEnum GOV_FIXED = new ProtocolTypeEnum("GOV_FIXED", "政府固点");
    
    public static final ProtocolTypeEnum TRIPARTITE_FIXED = new ProtocolTypeEnum("TRIPARTITE_FIXED", "三方固点");

    static {
        mappings.put("SETTLE_PRO", SETTLE_PRO);
        mappings.put("MANAGE_PRO", MANAGE_PRO);
        mappings.put("PARTNER_QUIT_PRO", PARTNER_QUIT_PRO);
        mappings.put("GOV_FIXED", GOV_FIXED);
        mappings.put("TRIPARTITE_FIXED", TRIPARTITE_FIXED);
    }

    public ProtocolTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ProtocolTypeEnum() {

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

    public static ProtocolTypeEnum valueof(String code) {
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
        ProtocolTypeEnum other = (ProtocolTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}