package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 资产出库单状态枚举
 * @author quanzhu.wangqz
 *
 */
public class AssetRolloutStatusEnum implements Serializable{


    private static final Map<String, AssetRolloutStatusEnum> mappings = new HashMap<String, AssetRolloutStatusEnum>();


    private static final long serialVersionUID = -2325045809951928493L;

    private String code;
    private String desc;
    
    public static final AssetRolloutStatusEnum WAIT_ROLLOUT  = new AssetRolloutStatusEnum("WAIT_ROLLOUT", "待对方入库");
    public static final AssetRolloutStatusEnum ROLLOUT_ING = new AssetRolloutStatusEnum("ROLLOUT_ING", "对方部分入库");
    public static final AssetRolloutStatusEnum ROLLOUT_DONE = new AssetRolloutStatusEnum("ROLLOUT_DONE", "对方全部入库");
    public static final AssetRolloutStatusEnum CANCEL = new AssetRolloutStatusEnum("CANCEL", "已撤回");
    public static final AssetRolloutStatusEnum WAIT_AUDIT = new AssetRolloutStatusEnum("WAIT_AUDIT", "待审批");
    public static final AssetRolloutStatusEnum AUDIT_NOT_PASS = new AssetRolloutStatusEnum("AUDIT_NOT_PASS", "审批不通过");
    public static final AssetRolloutStatusEnum WAIT_COMPENSATE = new AssetRolloutStatusEnum("WAIT_COMPENSATE", "待赔付");
    //public static final AssetRolloutStatusEnum COMPENSATE_ING = new AssetRolloutStatusEnum("COMPENSATE_ING", "赔付中");
    public static final AssetRolloutStatusEnum COMPENSATE_DONE = new AssetRolloutStatusEnum("COMPENSATE_DONE", "已赔付");



    
    static {
    	mappings.put("WAIT_ROLLOUT", WAIT_ROLLOUT);
        mappings.put("ROLLOUT_ING", ROLLOUT_ING);
        mappings.put("ROLLOUT_DONE", ROLLOUT_DONE);
        mappings.put("CANCEL", CANCEL);
        mappings.put("WAIT_AUDIT", WAIT_AUDIT);
        mappings.put("AUDIT_NOT_PASS", AUDIT_NOT_PASS);
        mappings.put("WAIT_COMPENSATE", WAIT_COMPENSATE);
        mappings.put("COMPENSATE_DONE", COMPENSATE_DONE);
    }

    public AssetRolloutStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AssetRolloutStatusEnum() {

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

    public static AssetRolloutStatusEnum valueof(String code) {
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
        AssetRolloutStatusEnum other = (AssetRolloutStatusEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
