package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 出入库详情 签收状态枚举
 * @author quanzhu.wangqz
 *
 */
public class AssetRolloutIncomeDetailStatusEnum implements Serializable{

	private static final Map<String, AssetRolloutIncomeDetailStatusEnum> mappings = new HashMap<String, AssetRolloutIncomeDetailStatusEnum>();


    private static final long serialVersionUID = -2325045809951928493L;

    private String code;
    private String desc;
    
    public static final AssetRolloutIncomeDetailStatusEnum WAIT_SIGN  = new AssetRolloutIncomeDetailStatusEnum("WAIT_SIGN", "待签收");
    public static final AssetRolloutIncomeDetailStatusEnum HAS_SIGN = new AssetRolloutIncomeDetailStatusEnum("HAS_SIGN", "已签收");
    public static final AssetRolloutIncomeDetailStatusEnum CANCEL = new AssetRolloutIncomeDetailStatusEnum("CANCEL", "已取消");


    static {
    	mappings.put("WAIT_SIGN", WAIT_SIGN);
        mappings.put("HAS_SIGN", HAS_SIGN);
        mappings.put("CANCEL", CANCEL);
    }

    public AssetRolloutIncomeDetailStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AssetRolloutIncomeDetailStatusEnum() {

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

    public static AssetRolloutIncomeDetailStatusEnum valueof(String code) {
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
        AssetRolloutIncomeDetailStatusEnum other = (AssetRolloutIncomeDetailStatusEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
