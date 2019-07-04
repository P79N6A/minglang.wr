package com.taobao.cun.auge.cuncounty.dto;

import java.util.HashMap;
import java.util.Map;

public class CuntaoCountyTagEnum {
	private String code;
    private String desc;

    private static final Map<String, CuntaoCountyTagEnum> MAPPINGS = new HashMap<String, CuntaoCountyTagEnum>();

    public static final CuntaoCountyTagEnum protocolWillExpire = new CuntaoCountyTagEnum("protocolWillExpire", "协议临期");
    public static final CuntaoCountyTagEnum protocolNotExists = new CuntaoCountyTagEnum("protocolNotExists", "未签协议");
    public static final CuntaoCountyTagEnum protocolMaybeNotExists = new CuntaoCountyTagEnum("protocolMaybeNotExists", "疑似未签协议");

    static {
    	MAPPINGS.put("protocolWillExpire", protocolWillExpire);
    	MAPPINGS.put("protocolNotExists", protocolNotExists);
    	MAPPINGS.put("protocolMaybeNotExists", protocolMaybeNotExists);
    }

    public CuntaoCountyTagEnum(String code, String desc) {
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

	public static CuntaoCountyTagEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return MAPPINGS.get(code);
    }
}
