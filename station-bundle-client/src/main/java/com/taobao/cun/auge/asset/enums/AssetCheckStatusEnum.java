package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 资产盘点状态枚举
 * @author quanzhu.wangqz
 *
 */
public class AssetCheckStatusEnum implements Serializable{


    private static final Map<String, AssetCheckStatusEnum> mappings = new HashMap<String, AssetCheckStatusEnum>();


    private static final long serialVersionUID = -2325045809951928493L;

    private String code;
    private String desc;
    
    public static final AssetCheckStatusEnum UNCHECKED  = new AssetCheckStatusEnum("UNCHECKED", "未启动");
    public static final AssetCheckStatusEnum CHECKING  = new AssetCheckStatusEnum("CHECKING", "待盘点");
    public static final AssetCheckStatusEnum CHECKED = new AssetCheckStatusEnum("CHECKED", "已盘点");


    static {
    	mappings.put("UNCHECKED", UNCHECKED);
    	mappings.put("CHECKING", CHECKING);
        mappings.put("CHECKED", CHECKED);
    }

    public AssetCheckStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AssetCheckStatusEnum() {

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

    public static AssetCheckStatusEnum valueof(String code) {
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
        AssetCheckStatusEnum other = (AssetCheckStatusEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
