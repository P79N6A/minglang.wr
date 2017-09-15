package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AssetIncomeSignTypeEnum implements Serializable{
	private static final Map<String, AssetIncomeSignTypeEnum> mappings = new HashMap<String, AssetIncomeSignTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;
    
    public static final AssetIncomeSignTypeEnum SCAN  = new AssetIncomeSignTypeEnum("SCAN", "扫码签收");
    public static final AssetIncomeSignTypeEnum CONFIRM = new AssetIncomeSignTypeEnum("CONFIRM", "一键确认");


    static {
    	mappings.put("SCAN", SCAN);
        mappings.put("CONFIRM", CONFIRM);
    }

    public AssetIncomeSignTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AssetIncomeSignTypeEnum() {

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

    public static AssetIncomeSignTypeEnum valueof(String code) {
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
        AssetIncomeSignTypeEnum other = (AssetIncomeSignTypeEnum) obj;
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
