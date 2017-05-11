package com.taobao.cun.auge.asset.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class AssetFlowOrderByEnum implements Serializable{

	private static final long serialVersionUID = -6095122805829041421L;

    /**
     * 申请时间排序
     */
    public static final AssetFlowOrderByEnum APPLY_TIME_ASC = new AssetFlowOrderByEnum(
            "apply_time_asc", "apply_time asc");
    public static final AssetFlowOrderByEnum APPLY_TIME_DESC = new AssetFlowOrderByEnum(
            "apply_time_desc", "apply_time desc");

    private static final Map<String, AssetFlowOrderByEnum> mappings = new HashMap<String, AssetFlowOrderByEnum>();

    static {
        mappings.put("apply_time_asc", APPLY_TIME_ASC);
        mappings.put("apply_time_asc", APPLY_TIME_DESC);

    }

    private String code;
    private String value;

    @SuppressWarnings("unused")
    private AssetFlowOrderByEnum() {

    }

    public AssetFlowOrderByEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        if (obj == null)
            return false;

        if (!(obj instanceof AssetFlowOrderByEnum))
            return false;

        AssetFlowOrderByEnum objType = (AssetFlowOrderByEnum) obj;
        return objType.getCode().equals(this.getCode());
    }

    public static AssetFlowOrderByEnum valueof(String code) {
        if (code == null)
            return null;
        return mappings.get(code);
    }
}
