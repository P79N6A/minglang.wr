package com.taobao.cun.auge.contactrecord.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 拜访方式
 */
public class CuntaoGovContactRecordWayEnum {
	private String code;
    private String desc;

    private static final Map<String, CuntaoGovContactRecordWayEnum> MAPPINGS = new HashMap<String, CuntaoGovContactRecordWayEnum>();

    public static final CuntaoGovContactRecordWayEnum DINGTALK = new CuntaoGovContactRecordWayEnum("DINGTALK", "钉钉");
    public static final CuntaoGovContactRecordWayEnum TELE = new CuntaoGovContactRecordWayEnum("TELE", "电话");
    public static final CuntaoGovContactRecordWayEnum DOOR_TO_DOOR = new CuntaoGovContactRecordWayEnum("DOOR_TO_DOOR", "上门");

    static {
    	MAPPINGS.put("DINGTALK", DINGTALK);
    	MAPPINGS.put("TELE", TELE);
    	MAPPINGS.put("DOOR_TO_DOOR", DOOR_TO_DOOR);
    }

    public CuntaoGovContactRecordWayEnum(String code, String desc) {
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

	public static CuntaoGovContactRecordWayEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return MAPPINGS.get(code);
    }
}
