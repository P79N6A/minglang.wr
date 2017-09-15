package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 出入库详情 签收状态枚举
 * @author quanzhu.wangqz
 *
 */
public class AssetRolloutIncomeDetailTypeEnum implements Serializable{

	private static final Map<String, AssetRolloutIncomeDetailTypeEnum> mappings = new HashMap<String, AssetRolloutIncomeDetailTypeEnum>();


    private static final long serialVersionUID = -2325045809951928493L;

    private String code;
    private String desc;
    
    public static final AssetRolloutIncomeDetailTypeEnum DISTRIBUTION  = new AssetRolloutIncomeDetailTypeEnum("DISTRIBUTION", "分发");
    public static final AssetRolloutIncomeDetailTypeEnum SCRAP = new AssetRolloutIncomeDetailTypeEnum("SCRAP", "遗失损毁");
    public static final AssetRolloutIncomeDetailTypeEnum PURCHASE  = new AssetRolloutIncomeDetailTypeEnum("PURCHASE", "采购");
    public static final AssetRolloutIncomeDetailTypeEnum TRANSFER = new AssetRolloutIncomeDetailTypeEnum("TRANSFER", "转移");
    public static final AssetRolloutIncomeDetailTypeEnum RECOVER = new AssetRolloutIncomeDetailTypeEnum("RECOVER", "回收");




    static {
    	mappings.put("DISTRIBUTION", DISTRIBUTION);
        mappings.put("SCRAP", SCRAP);
        mappings.put("PURCHASE", PURCHASE);
        mappings.put("TRANSFER", TRANSFER);
        mappings.put("RECOVER", RECOVER);
    }

    public AssetRolloutIncomeDetailTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AssetRolloutIncomeDetailTypeEnum() {

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

    public static AssetRolloutIncomeDetailTypeEnum valueof(String code) {
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
        AssetRolloutIncomeDetailTypeEnum other = (AssetRolloutIncomeDetailTypeEnum) obj;
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
