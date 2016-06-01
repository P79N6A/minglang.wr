package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class AccountMoneyTargetTypeEnum implements Serializable {

    private static final Map<String, AccountMoneyTargetTypeEnum> mappings = new HashMap<String, AccountMoneyTargetTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;

    public static final AccountMoneyTargetTypeEnum PARTNER_INSTANCE = new AccountMoneyTargetTypeEnum("PARTNER_INSTANCE", "合伙人实例");


    static {
        mappings.put("PARTNER_INSTANCE", PARTNER_INSTANCE);
    }

    public AccountMoneyTargetTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public AccountMoneyTargetTypeEnum() {

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

    public static AccountMoneyTargetTypeEnum valueof(String code) {
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
        AccountMoneyTargetTypeEnum other = (AccountMoneyTargetTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
