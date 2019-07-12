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

    public static final CuntaoGovContactRecordWayEnum ONLINE = new CuntaoGovContactRecordWayEnum("ONLINE", "线上拜访");
    public static final CuntaoGovContactRecordWayEnum GOV_VISIT = new CuntaoGovContactRecordWayEnum("GOV_VISIT", "政府来访");
    public static final CuntaoGovContactRecordWayEnum DOOR_TO_DOOR = new CuntaoGovContactRecordWayEnum("DOOR_TO_DOOR", "上门");
    public static final CuntaoGovContactRecordWayEnum OTHER = new CuntaoGovContactRecordWayEnum("OTHER", "其他");

    static {
        MAPPINGS.put("OTHER", OTHER);
    	MAPPINGS.put("ONLINE", ONLINE);
    	MAPPINGS.put("GOV_VISIT", GOV_VISIT);
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
