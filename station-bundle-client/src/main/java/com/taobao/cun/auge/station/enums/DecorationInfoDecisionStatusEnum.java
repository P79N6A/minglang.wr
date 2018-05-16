package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author alibaba-54766
 *
 */
public class DecorationInfoDecisionStatusEnum implements Serializable{

    private static final long serialVersionUID = 7770164546724440333L;
    
    // 待审核
    public static final DecorationInfoDecisionStatusEnum WAIT_AUDIT = new DecorationInfoDecisionStatusEnum("WAIT_AUDIT", "待审核");

    // 审核通过
    public static final DecorationInfoDecisionStatusEnum AUDIT_PASS = new DecorationInfoDecisionStatusEnum("AUDIT_PASS", "审核通过");
    
    // 审核不通过
    public static final DecorationInfoDecisionStatusEnum AUDIT_NOT_PASS = new DecorationInfoDecisionStatusEnum("AUDIT_NOT_PASS", "审核不通过");
   

    private static final Map<String, DecorationInfoDecisionStatusEnum> mappings = new HashMap<String, DecorationInfoDecisionStatusEnum>();

    static {
        mappings.put("WAIT_AUDIT", WAIT_AUDIT);
        mappings.put("AUDIT_PASS", AUDIT_PASS);
        mappings.put("AUDIT_NOT_PASS", AUDIT_NOT_PASS);
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(obj instanceof DecorationInfoDecisionStatusEnum)) {
            return false;
        }
        DecorationInfoDecisionStatusEnum objType = (DecorationInfoDecisionStatusEnum) obj;
        return objType.getCode().equals(this.getCode());
    }

    public DecorationInfoDecisionStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public DecorationInfoDecisionStatusEnum() {
        
    }
    public static DecorationInfoDecisionStatusEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return mappings.get(code);
    }

}
