package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 资产入库单类型枚举
 * @author quanzhu.wangqz
 *
 */
public class AssetIncomeTypeEnum implements Serializable{


    private static final Map<String, AssetIncomeTypeEnum> mappings = new HashMap<String, AssetIncomeTypeEnum>();


    private static final long serialVersionUID = -2325045809951928493L;

    private String code;
    private String desc;
    
    public static final AssetIncomeTypeEnum PURCHASE  = new AssetIncomeTypeEnum("PURCHASE", "采购");
    public static final AssetIncomeTypeEnum TRANSFER = new AssetIncomeTypeEnum("TRANSFER", "转移");
    public static final AssetIncomeTypeEnum RECOVER = new AssetIncomeTypeEnum("RECOVER", "回收");


    static {
    	mappings.put("PURCHASE", PURCHASE);
        mappings.put("TRANSFER", TRANSFER);
        mappings.put("RECOVER", RECOVER);
    }

    public AssetIncomeTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AssetIncomeTypeEnum() {

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

    public static AssetIncomeTypeEnum valueof(String code) {
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
        AssetIncomeTypeEnum other = (AssetIncomeTypeEnum) obj;
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
