package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NewRevenueCommunicationBusinessTypeEnum implements Serializable {

    public static NewRevenueCommunicationBusinessTypeEnum REVENUE_INVITE=new NewRevenueCommunicationBusinessTypeEnum("REVENUE_INVITE","切换邀约");
    public static NewRevenueCommunicationBusinessTypeEnum TRANS_INVITE=new NewRevenueCommunicationBusinessTypeEnum("TRANS_INVITE","转型邀约");


    private static final Map<String, NewRevenueCommunicationBusinessTypeEnum> mappings = new HashMap<String, NewRevenueCommunicationBusinessTypeEnum>();
    static {
        mappings.put("REVENUE_INVITE", REVENUE_INVITE);
        mappings.put("TRANS_INVITE", TRANS_INVITE);
    }

    private String code;
    private String desc;

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
        if (!(obj instanceof PartnerInstanceRevenueStatusEnum)) {
            return false;
        }
        PartnerInstanceRevenueStatusEnum objType = (PartnerInstanceRevenueStatusEnum) obj;
        return objType.getCode().equals(this.getCode());
    }

    public NewRevenueCommunicationBusinessTypeEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public static NewRevenueCommunicationBusinessTypeEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return mappings.get(code);
    }


    @SuppressWarnings("unused")
    private NewRevenueCommunicationBusinessTypeEnum() {

    }



}
