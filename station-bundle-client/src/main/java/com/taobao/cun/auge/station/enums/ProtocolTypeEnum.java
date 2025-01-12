package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProtocolTypeEnum implements Serializable {
 
	private static final long serialVersionUID = -3778546477529242130L;

	private static final Map<String, ProtocolTypeEnum> mappings = new HashMap<String, ProtocolTypeEnum>();

    private String code;
    private String desc;

    public static final ProtocolTypeEnum SETTLE_PRO = new ProtocolTypeEnum("SETTLE_PRO", "入驻协议");
    
    public static final ProtocolTypeEnum C2B_SETTLE_PRO = new ProtocolTypeEnum("C2B_SETTLE_PRO", "C转B入驻协议");


    public static final ProtocolTypeEnum MANAGE_PRO = new ProtocolTypeEnum("MANAGE_PRO", "管理协议");

    public static final ProtocolTypeEnum PARTNER_QUIT_PRO = new ProtocolTypeEnum("PARTNER_QUIT_PRO", "合伙人退出协议");

    public static final ProtocolTypeEnum GOV_FIXED = new ProtocolTypeEnum("GOV_FIXED", "政府固点");
    
    public static final ProtocolTypeEnum TRIPARTITE_FIXED = new ProtocolTypeEnum("TRIPARTITE_FIXED", "三方固点");

    public static final ProtocolTypeEnum COURSE_SCHEDULE = new ProtocolTypeEnum("COURSE_SCHEDULE", "公益课程协议");
    
    public static final ProtocolTypeEnum ALIPAY_AGREEMENT = new ProtocolTypeEnum("ALIPAY_AGREEMENT", "支付宝协议支付");
    
    public static final ProtocolTypeEnum SAMPLE_GOODS_AGREEMENT = new ProtocolTypeEnum("SAMPLE_GOODS_AGREEMENT", "拍样协议");

    public static final ProtocolTypeEnum SUPPLY_GOODS_AGREEMENT = new ProtocolTypeEnum("SUPPLY_GOODS_AGREEMENT", "补货协议");
    
    public static final ProtocolTypeEnum STORE_SUPPLY_GOODS_AGREEMENT = new ProtocolTypeEnum("STORE_SUPPLY_GOODS_AGREEMENT", "门店补货协议");

    public static final ProtocolTypeEnum STATION_OPENING_AGREEMENT = new ProtocolTypeEnum("STATION_OPENING_AGREEMENT", "村点开业包协议");

    public static final ProtocolTypeEnum VENDOR_DISTRIBUTE_AGREEMENT = new ProtocolTypeEnum("VENDOR_DISTRIBUTE_AGREEMENT", "服务商送货上门包协议");

    public static final ProtocolTypeEnum VENDOR_INSTALLMENT_AGREEMENT = new ProtocolTypeEnum("VENDOR_INSTALLMENT_AGREEMENT", "服务商送装协议");

    public static final ProtocolTypeEnum RUBBING_SUPPLEMENT_AGREEMENT = new ProtocolTypeEnum("RUBBING_SUPPLEMENT_AGREEMENT", "合伙人蹭单补充协议");

    public static final ProtocolTypeEnum DELIVERY_GOODS_AGREEMENT = new ProtocolTypeEnum("DELIVERY_GOODS_AGREEMENT", "送货上门协议");

    public static final ProtocolTypeEnum CTS_JZ_SZYT_AGREEMENT = new ProtocolTypeEnum("CTS_JZ_SZYT_AGREEMENT", "家装送装协议");
    
    public static final ProtocolTypeEnum PARTNER_APPLY_PROJECT_NOTICE = new ProtocolTypeEnum("PARTNER_APPLY_PROJECT_NOTICE", "招募项目告知协议");
    public static final ProtocolTypeEnum PARTNER_APPLY_PROJECT_NOTICE_TPS = new ProtocolTypeEnum("PARTNER_APPLY_PROJECT_NOTICE_TPS", "体验店招募项目告知协议");
    public static final ProtocolTypeEnum PARTNER_APPLY_MANAGER_STANDARD_TPS = new ProtocolTypeEnum("PARTNER_APPLY_MANAGER_STANDARD_TPS", "体验店管理规范协议");

    
    public static final ProtocolTypeEnum PARTNER_QUIT_PRO_TPS = new ProtocolTypeEnum("PARTNER_QUIT_PRO_TPS", "体验店合伙人退出协议");

    public static final ProtocolTypeEnum EXPERIENCE_GOODS_SUPPLEMENT_AGREEMENT = new ProtocolTypeEnum("EXPERIENCE_GOODS_SUPPLEMENT_AGREEMENT", "体验货品补货协议");

    public static final ProtocolTypeEnum YOUPIN_FMCG_GOODS_REPLENISHMENT_AGREEMENT = new ProtocolTypeEnum("YOUPIN_FMCG_GOODS_REPLENISHMENT_AGREEMENT", "优品快消货品进货协议");

    public static final ProtocolTypeEnum YOUPIN_SELEC_GOODS_REPLENISHMENT_AGREEMENT = new ProtocolTypeEnum("YOUPIN_SELEC_GOODS_REPLENISHMENT_AGREEMENT", "优品小家电货品进货协议");

    public static final ProtocolTypeEnum UM_SETTLING = new ProtocolTypeEnum("UM_SETTLING", "优盟入驻协议");

    static {
        mappings.put("SETTLE_PRO", SETTLE_PRO);
        mappings.put("MANAGE_PRO", MANAGE_PRO);
        mappings.put("PARTNER_QUIT_PRO", PARTNER_QUIT_PRO);
        mappings.put("GOV_FIXED", GOV_FIXED);
        mappings.put("TRIPARTITE_FIXED", TRIPARTITE_FIXED);
        mappings.put("COURSE_SCHEDULE", COURSE_SCHEDULE);
        mappings.put("C2B_SETTLE_PRO", C2B_SETTLE_PRO);
        mappings.put("ALIPAY_AGREEMENT", ALIPAY_AGREEMENT);
        mappings.put("SAMPLE_GOODS_AGREEMENT", SAMPLE_GOODS_AGREEMENT);
        mappings.put("SUPPLY_GOODS_AGREEMENT", SUPPLY_GOODS_AGREEMENT);
        mappings.put("STORE_SUPPLY_GOODS_AGREEMENT", STORE_SUPPLY_GOODS_AGREEMENT);
        mappings.put("STATION_OPENING_AGREEMENT", STATION_OPENING_AGREEMENT);
        mappings.put("VENDOR_DISTRIBUTE_AGREEMENT", VENDOR_DISTRIBUTE_AGREEMENT);
        mappings.put("VENDOR_INSTALLMENT_AGREEMENT", VENDOR_INSTALLMENT_AGREEMENT);
        mappings.put("RUBBING_SUPPLEMENT_AGREEMENT", RUBBING_SUPPLEMENT_AGREEMENT);
        mappings.put("DELIVERY_GOODS_AGREEMENT", DELIVERY_GOODS_AGREEMENT);
        mappings.put("PARTNER_APPLY_PROJECT_NOTICE", PARTNER_APPLY_PROJECT_NOTICE);
        mappings.put("EXPERIENCE_GOODS_SUPPLEMENT_AGREEMENT",EXPERIENCE_GOODS_SUPPLEMENT_AGREEMENT);
        mappings.put("YOUPIN_FMCG_GOODS_REPLENISHMENT_AGREEMENT", YOUPIN_FMCG_GOODS_REPLENISHMENT_AGREEMENT);
        mappings.put("YOUPIN_SELEC_GOODS_REPLENISHMENT_AGREEMENT", YOUPIN_SELEC_GOODS_REPLENISHMENT_AGREEMENT);
        mappings.put("UM_SETTLING", UM_SETTLING);
        mappings.put("PARTNER_APPLY_PROJECT_NOTICE_TPS", PARTNER_APPLY_PROJECT_NOTICE_TPS);
        mappings.put("PARTNER_APPLY_MANAGER_STANDARD_TPS", PARTNER_APPLY_MANAGER_STANDARD_TPS);
        mappings.put("PARTNER_QUIT_PRO_TPS", PARTNER_QUIT_PRO_TPS);
        mappings.put("CTS_JZ_SZYT_AGREEMENT",CTS_JZ_SZYT_AGREEMENT);


    }

    public ProtocolTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ProtocolTypeEnum() {

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

    public static ProtocolTypeEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return mappings.get(code);
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ProtocolTypeEnum other = (ProtocolTypeEnum) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }
}