package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class AccountMoneyStateEnum implements Serializable {

    private static final Map<String, AccountMoneyStateEnum> mappings = new HashMap<String, AccountMoneyStateEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;
    
    public static final AccountMoneyStateEnum WAIT_FROZEN  = new AccountMoneyStateEnum("WAIT_FROZEN", "待冻结");
    
    public static final AccountMoneyStateEnum HAS_FROZEN = new AccountMoneyStateEnum("HAS_FROZEN", "已冻结");
    public static final AccountMoneyStateEnum HAS_THAW = new AccountMoneyStateEnum("HAS_THAW", "已解冻");


    static {
    	mappings.put("WAIT_FROZEN", WAIT_FROZEN);
        mappings.put("HAS_FROZEN", HAS_FROZEN);
        mappings.put("HAS_THAW", HAS_THAW);
    }

    public AccountMoneyStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AccountMoneyStateEnum() {

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

    public static AccountMoneyStateEnum valueof(String code) {
        if (code == null)
            return null;
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccountMoneyStateEnum other = (AccountMoneyStateEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
