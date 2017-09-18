package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AssetRolloutReceiverAreaTypeEnum implements Serializable{
	private static final Map<String, AssetRolloutReceiverAreaTypeEnum> mappings = new HashMap<String, AssetRolloutReceiverAreaTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;
    
    public static final AssetRolloutReceiverAreaTypeEnum COUNTY = new AssetRolloutReceiverAreaTypeEnum("COUNTY", "县服务中心");
    public static final AssetRolloutReceiverAreaTypeEnum STATION = new AssetRolloutReceiverAreaTypeEnum("STATION", "服务站");

    static {
        mappings.put("COUNTY", COUNTY);
        mappings.put("STATION", STATION);
    }

    public AssetRolloutReceiverAreaTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AssetRolloutReceiverAreaTypeEnum() {

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

    public static AssetRolloutReceiverAreaTypeEnum valueof(String code) {
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
        AssetRolloutReceiverAreaTypeEnum other = (AssetRolloutReceiverAreaTypeEnum) obj;
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
