package com.taobao.cun.auge.asset.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CuntaoAssetEnum implements Serializable{

	private static final long serialVersionUID = -8688415975088634609L;
	
	public static final CuntaoAssetEnum COUNTY_SIGN = new CuntaoAssetEnum(
            "COUNTY_SIGN", "县签收");
    public static final CuntaoAssetEnum WAIT_STATION_SIGN = new CuntaoAssetEnum(
            "WAIT_STATION_SIGN", "待村点签收");
    public static final CuntaoAssetEnum STATION_SIGN = new CuntaoAssetEnum(
            "STATION_SIGN", "村已签收");
    public static final CuntaoAssetEnum UNSIGN = new CuntaoAssetEnum(
            "UNSIGN", "未签收");
    public static final CuntaoAssetEnum UNMATCH = new CuntaoAssetEnum(
            "UNMATCH", "未匹配");
    public static final CuntaoAssetEnum DISTRIBUTION = new CuntaoAssetEnum(
            "WAIT_STATION_SIGN", "分发");
    public static final CuntaoAssetEnum CHECKED = new CuntaoAssetEnum(
            "CHECKED", "已盘点");
    public static final CuntaoAssetEnum UNCHECKED = new CuntaoAssetEnum(
            "UNCHECKED", "未启动");
    public static final CuntaoAssetEnum CHECKING = new CuntaoAssetEnum(
            "CHECKING", "待盘点");
    public static final CuntaoAssetEnum WORKER = new CuntaoAssetEnum(
            "WORKER", "员工");
    public static final CuntaoAssetEnum PARTNER = new CuntaoAssetEnum(
            "PARTNER", "合伙人");
    private static final Map<String, CuntaoAssetEnum> mappings = new HashMap<String, CuntaoAssetEnum>();
    static {
        mappings.put("COUNTY_SIGN", COUNTY_SIGN);
        mappings.put("WAIT_STATION_SIGN", WAIT_STATION_SIGN);
        mappings.put("STATION_SIGN", STATION_SIGN);
        mappings.put("UNSIGN", UNSIGN);
        mappings.put("UNMATCH", UNMATCH);
        mappings.put("UNCHECKED", UNCHECKED);
        mappings.put("CHECKED", CHECKED);
        mappings.put("CHECKING", CHECKING);
        mappings.put("WORKER", WORKER);
        mappings.put("PARTNER", PARTNER);
        mappings.put("DISTRIBUTION", DISTRIBUTION);
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CuntaoAssetEnum)) {
            return false;
        }
        CuntaoAssetEnum objType = (CuntaoAssetEnum) obj;
        return objType.getCode().equals(this.getCode());
    }


    public static CuntaoAssetEnum valueof(String code) {
        if (code==null) {
            return null;
        }
        return mappings.get(code);
    }


    public CuntaoAssetEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
