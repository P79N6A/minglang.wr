package com.taobao.cun.auge.cuncounty.dto;

import java.util.HashMap;
import java.util.Map;

public class CuntaoCountyProtocolRiskEnum {
	private String code;
    private String desc;

    private static final Map<String, CuntaoCountyProtocolRiskEnum> MAPPINGS = new HashMap<String, CuntaoCountyProtocolRiskEnum>();

    public static final CuntaoCountyProtocolRiskEnum protocolWillExpire = new CuntaoCountyProtocolRiskEnum("protocolWillExpire", "协议临期");
    public static final CuntaoCountyProtocolRiskEnum protocolNotExists = new CuntaoCountyProtocolRiskEnum("protocolNotExists", "未签协议");
    public static final CuntaoCountyProtocolRiskEnum protocolMaybeNotExists = new CuntaoCountyProtocolRiskEnum("protocolMaybeNotExists", "疑似未签协议");

    static {
    	MAPPINGS.put("protocolWillExpire", protocolWillExpire);
    	MAPPINGS.put("protocolNotExists", protocolNotExists);
    	MAPPINGS.put("protocolMaybeNotExists", protocolMaybeNotExists);
    }

    public CuntaoCountyProtocolRiskEnum(String code, String desc) {
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

	public static CuntaoCountyProtocolRiskEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return MAPPINGS.get(code);
    }
}
