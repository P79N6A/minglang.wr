package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 资产出库单类型枚举
 * @author quanzhu.wangqz
 *
 */
public class AssetRolloutTypeEnum implements Serializable{


    private static final Map<String, AssetRolloutTypeEnum> mappings = new HashMap<String, AssetRolloutTypeEnum>();


    private static final long serialVersionUID = -2325045809951928493L;

    private String code;
    private String desc;
    
    public static final AssetRolloutTypeEnum DISTRIBUTION  = new AssetRolloutTypeEnum("DISTRIBUTION", "分发");
    public static final AssetRolloutTypeEnum TRANSFER = new AssetRolloutTypeEnum("TRANSFER", "转移");
    public static final AssetRolloutTypeEnum SCRAP = new AssetRolloutTypeEnum("SCRAP", "遗失损毁");


    static {
    	mappings.put("DISTRIBUTION", DISTRIBUTION);
        mappings.put("TRANSFER", TRANSFER);
        mappings.put("SCRAP", SCRAP);
    }

    public AssetRolloutTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AssetRolloutTypeEnum() {

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

    public static AssetRolloutTypeEnum valueof(String code) {
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
        AssetRolloutTypeEnum other = (AssetRolloutTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
