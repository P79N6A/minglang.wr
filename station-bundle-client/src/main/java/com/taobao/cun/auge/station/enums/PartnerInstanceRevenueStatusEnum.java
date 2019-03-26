package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PartnerInstanceRevenueStatusEnum implements Serializable {


    public static PartnerInstanceRevenueStatusEnum WAIT_REVENUE_TRANS=new PartnerInstanceRevenueStatusEnum("WAIT_REVENUE_TRANS","等待收入切换");
    public static PartnerInstanceRevenueStatusEnum REVENUE_TRANS_DONE=new PartnerInstanceRevenueStatusEnum("REVENUE_TRANS_DONE","收入切换完成");


    private static final Map<String, PartnerInstanceRevenueStatusEnum> mappings = new HashMap<String, PartnerInstanceRevenueStatusEnum>();
    static {
        mappings.put("WAIT_REVENUE_TRANS", WAIT_REVENUE_TRANS);
        mappings.put("REVENUE_TRANS_DONE", REVENUE_TRANS_DONE);
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

    public PartnerInstanceRevenueStatusEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public static PartnerInstanceRevenueStatusEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return mappings.get(code);
    }


    @SuppressWarnings("unused")
    private PartnerInstanceRevenueStatusEnum() {

    }

}
