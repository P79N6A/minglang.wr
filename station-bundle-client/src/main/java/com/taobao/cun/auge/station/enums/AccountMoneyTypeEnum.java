package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by haihu.fhh on 2016/5/4.
 */
public class AccountMoneyTypeEnum implements Serializable {

    private static final Map<String, AccountMoneyTypeEnum> mappings = new HashMap<String, AccountMoneyTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;

    //保证金
    public static final AccountMoneyTypeEnum PARTNER_BOND = new AccountMoneyTypeEnum("PARTNER_BOND", "保证金");


    static {
        mappings.put("PARTNER_BOND", PARTNER_BOND);
    }

    public AccountMoneyTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AccountMoneyTypeEnum() {

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

    public static AccountMoneyTypeEnum valueof(String code) {
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
        AccountMoneyTypeEnum other = (AccountMoneyTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
