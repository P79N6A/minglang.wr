package com.taobao.cun.auge.contactrecord.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 风险是否解决
 */
public class CuntaoGovContactRecordStateEnum {
	private String code;
    private String desc;

    private static final Map<String, CuntaoGovContactRecordStateEnum> MAPPINGS = new HashMap<String, CuntaoGovContactRecordStateEnum>();

    public static final CuntaoGovContactRecordStateEnum RESOLVED = new CuntaoGovContactRecordStateEnum("RESOLVED", "已解决");
    public static final CuntaoGovContactRecordStateEnum UNRESOLVED = new CuntaoGovContactRecordStateEnum("UNRESOLVED", "未解决");

    static {
    	MAPPINGS.put("RESOLVED", RESOLVED);
    	MAPPINGS.put("UNRESOLVED", UNRESOLVED);
    }

    public CuntaoGovContactRecordStateEnum(String code, String desc) {
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

	public static CuntaoGovContactRecordStateEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return MAPPINGS.get(code);
    }
}
