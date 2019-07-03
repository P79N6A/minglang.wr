package com.taobao.cun.auge.contactrecord.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 风险问题
 * 高风险对应（政府发函、合同履约、农资纠纷、收改调整、社会纠纷、场地闲置、其它情形）
 *
 * 中风险对应（对接失联、政府约谈、合同履约、合同到期、其它情形）
 *
 * 低风险（上行述求、数据述求、合同续签、其它情形）
 */
public class CuntaoGovContactRiskTypeEnum {
	private String code;
    private String desc;

    private static final Map<String, CuntaoGovContactRiskTypeEnum> MAPPINGS = new HashMap<String, CuntaoGovContactRiskTypeEnum>();

    public static final CuntaoGovContactRiskTypeEnum HIGH_GOV_LETTER = new CuntaoGovContactRiskTypeEnum("HIGH_GOV_LETTER", "政府发函");
    public static final CuntaoGovContactRiskTypeEnum HIGH_KEEP_CONTRACT = new CuntaoGovContactRiskTypeEnum("HIGH_KEEP_CONTRACT", "合同履约");
    public static final CuntaoGovContactRiskTypeEnum HIGH_NZ_DISPUTE = new CuntaoGovContactRiskTypeEnum("HIGH_NZ_DISPUTE", "农资纠纷");
    public static final CuntaoGovContactRiskTypeEnum HIGH_SGTZ = new CuntaoGovContactRiskTypeEnum("HIGH_SGTZ", "收改调整");
    public static final CuntaoGovContactRiskTypeEnum HIGH_SHJF = new CuntaoGovContactRiskTypeEnum("HIGH_SH", "社会纠纷");
    public static final CuntaoGovContactRiskTypeEnum HIGH_CDXZ = new CuntaoGovContactRiskTypeEnum("HIGH_CD", "场地闲置");
    public static final CuntaoGovContactRiskTypeEnum HIGH_OTHER = new CuntaoGovContactRiskTypeEnum("HIGH_OTHER", "其它情形");

    public static final CuntaoGovContactRiskTypeEnum MIDDLE_DJSL = new CuntaoGovContactRiskTypeEnum("MIDDLE_DJSL", "对接失联");
    public static final CuntaoGovContactRiskTypeEnum MIDDLE_ZFYT = new CuntaoGovContactRiskTypeEnum("MIDDLE_ZFYT", "政府约谈");
    public static final CuntaoGovContactRiskTypeEnum MIDDLE_KEEP_CONTRACT = new CuntaoGovContactRiskTypeEnum("MIDDLE_KEEP_CONTRACT", "合同履约");
    public static final CuntaoGovContactRiskTypeEnum MIDDLE_DUE_CONTRACT = new CuntaoGovContactRiskTypeEnum("MIDDLE_DUE_CONTRACT", "合同到期");
    public static final CuntaoGovContactRiskTypeEnum MIDDLE_OTHER = new CuntaoGovContactRiskTypeEnum("MIDDLE_OTHER", "其它情形");

    public static final CuntaoGovContactRiskTypeEnum LOW_SX = new CuntaoGovContactRiskTypeEnum("LOW_SX", "上行诉求");
    public static final CuntaoGovContactRiskTypeEnum LOW_DATA = new CuntaoGovContactRiskTypeEnum("LOW_DATA", "数据诉求");
    public static final CuntaoGovContactRiskTypeEnum LOW_RENEW_CONTRACT = new CuntaoGovContactRiskTypeEnum("LOW_RENEW_CONTRACT", "合同续签");
    public static final CuntaoGovContactRiskTypeEnum LOW_OTHER = new CuntaoGovContactRiskTypeEnum("LOW_OTHER", "其它情形");

    static {
    	MAPPINGS.put("HIGH_GOV_LETTER", HIGH_GOV_LETTER);
    	MAPPINGS.put("HIGH_KEEP_CONTRACT", HIGH_KEEP_CONTRACT);
    	MAPPINGS.put("HIGH_NZ_DISPUTE", HIGH_NZ_DISPUTE);
        MAPPINGS.put("HIGH_SGTZ", HIGH_SGTZ);
        MAPPINGS.put("HIGH_SHJF", HIGH_SHJF);
        MAPPINGS.put("HIGH_CDXZ", HIGH_CDXZ);
        MAPPINGS.put("HIGH_OTHER", HIGH_OTHER);
        MAPPINGS.put("MIDDLE_DJSL", MIDDLE_DJSL);
        MAPPINGS.put("MIDDLE_ZFYT", MIDDLE_ZFYT);
        MAPPINGS.put("MIDDLE_KEEP_CONTRACT", MIDDLE_KEEP_CONTRACT);
        MAPPINGS.put("MIDDLE_DUE_CONTRACT", MIDDLE_DUE_CONTRACT);
        MAPPINGS.put("MIDDLE_OTHER", MIDDLE_OTHER);
        MAPPINGS.put("LOW_SX", LOW_SX);
        MAPPINGS.put("LOW_DATA", LOW_DATA);
        MAPPINGS.put("LOW_RENEW_CONTRACT", LOW_RENEW_CONTRACT);
        MAPPINGS.put("LOW_OTHER", LOW_OTHER);
    }

    public CuntaoGovContactRiskTypeEnum(String code, String desc) {
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

	public static CuntaoGovContactRiskTypeEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return MAPPINGS.get(code);
    }
}
