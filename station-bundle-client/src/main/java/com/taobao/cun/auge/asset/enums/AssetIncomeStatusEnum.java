package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 资产入库单状态枚举
 * @author quanzhu.wangqz
 *
 */
public class AssetIncomeStatusEnum implements Serializable{


    private static final Map<String, AssetIncomeStatusEnum> mappings = new HashMap<String, AssetIncomeStatusEnum>();


    private static final long serialVersionUID = -2325045809951928493L;

    private String code;
    private String desc;
    
    public static final AssetIncomeStatusEnum TODO  = new AssetIncomeStatusEnum("TODO", "待入库");
    public static final AssetIncomeStatusEnum DOING = new AssetIncomeStatusEnum("DOING", "部分入库");
    public static final AssetIncomeStatusEnum DONE = new AssetIncomeStatusEnum("DONE", "全部入库");
    public static final AssetIncomeStatusEnum CANCEL = new AssetIncomeStatusEnum("CANCEL", "对方已撤回");

    
    static {
    	mappings.put("TODO", TODO);
        mappings.put("DOING", DOING);
        mappings.put("DONE", DONE);
        mappings.put("CANCEL", CANCEL);
    }

    public AssetIncomeStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AssetIncomeStatusEnum() {

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

    public static AssetIncomeStatusEnum valueof(String code) {
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
        AssetIncomeStatusEnum other = (AssetIncomeStatusEnum) obj;
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
