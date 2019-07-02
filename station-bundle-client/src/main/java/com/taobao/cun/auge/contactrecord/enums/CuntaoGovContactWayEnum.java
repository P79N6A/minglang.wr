package com.taobao.cun.auge.contactrecord.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 拜访方式
 */
public class CuntaoGovContactWayEnum {
	private String code;
    private String desc;

    private static final Map<String, CuntaoGovContactWayEnum> MAPPINGS = new HashMap<String, CuntaoGovContactWayEnum>();

    public static final CuntaoGovContactWayEnum DINGTALK = new CuntaoGovContactWayEnum("DINGTALK", "钉钉");
    public static final CuntaoGovContactWayEnum TELE = new CuntaoGovContactWayEnum("TELE", "电话");
    public static final CuntaoGovContactWayEnum DOOR_TO_DOOR = new CuntaoGovContactWayEnum("DOOR_TO_DOOR", "上门");

    static {
    	MAPPINGS.put("DINGTALK", DINGTALK);
    	MAPPINGS.put("TELE", TELE);
    	MAPPINGS.put("DOOR_TO_DOOR", DOOR_TO_DOOR);
    }

    public CuntaoGovContactWayEnum(String code, String desc) {
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

	public static CuntaoGovContactWayEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return MAPPINGS.get(code);
    }
}
