package com.taobao.cun.auge.contactrecord.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 风险等级
 */
public class CuntaoGovContactRiskLevelEnum {
	private String code;
    private String desc;

    private static final Map<String, CuntaoGovContactRiskLevelEnum> MAPPINGS = new HashMap<String, CuntaoGovContactRiskLevelEnum>();

    public static final CuntaoGovContactRiskLevelEnum HIGH = new CuntaoGovContactRiskLevelEnum("HIGH", "高");
    public static final CuntaoGovContactRiskLevelEnum MIDDLE = new CuntaoGovContactRiskLevelEnum("MIDDLE", "中");
    public static final CuntaoGovContactRiskLevelEnum LOW = new CuntaoGovContactRiskLevelEnum("LOW", "低");
    public static final CuntaoGovContactRiskLevelEnum NO = new CuntaoGovContactRiskLevelEnum("NO", "无");

    static {
    	MAPPINGS.put("HIGH", HIGH);
    	MAPPINGS.put("MIDDLE", MIDDLE);
    	MAPPINGS.put("LOW", LOW);
        MAPPINGS.put("NO", NO);
    }

    public CuntaoGovContactRiskLevelEnum(String code, String desc) {
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

	public static CuntaoGovContactRiskLevelEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return MAPPINGS.get(code);
    }
}
