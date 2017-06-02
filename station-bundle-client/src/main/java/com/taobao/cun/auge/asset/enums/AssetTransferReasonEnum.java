package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetTransferReasonEnum implements Serializable{

    private static final long serialVersionUID = -618247950096419977L;

    public static final AssetTransferReasonEnum LEAVE = new AssetTransferReasonEnum("LEAVE", "离职");

    public static final AssetTransferReasonEnum CHANGE = new AssetTransferReasonEnum("CHANGE", "转岗");

    public static final AssetTransferReasonEnum USE = new AssetTransferReasonEnum("USE", "资源利用");

    private static final Map<String, AssetTransferReasonEnum> mappings = new HashMap<String, AssetTransferReasonEnum>();

    static {
        mappings.put("LEAVE", LEAVE);
        mappings.put("CHANGE", CHANGE);
        mappings.put("USE", USE);
    }

    private String code;

    private String desc;

    public static AssetTransferReasonEnum valueOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return mappings.get(code);
    }

    public AssetTransferReasonEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
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

}
