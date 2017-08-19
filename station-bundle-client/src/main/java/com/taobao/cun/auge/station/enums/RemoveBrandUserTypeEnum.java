package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class RemoveBrandUserTypeEnum implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6044027946486112435L;

	private static final Map<String, RemoveBrandUserTypeEnum> mappings = new HashMap<String, RemoveBrandUserTypeEnum>();
    private String code;
    private String desc;
    
    public static final RemoveBrandUserTypeEnum TEMP  = new RemoveBrandUserTypeEnum("TEMP", "暂不拆除");
    
    public static final RemoveBrandUserTypeEnum NONE  = new RemoveBrandUserTypeEnum("NONE", "不拆除");
    
    public static final RemoveBrandUserTypeEnum PARTNER  = new RemoveBrandUserTypeEnum("PARTNER", "村小二");
    
    public static final RemoveBrandUserTypeEnum ISV = new RemoveBrandUserTypeEnum("ISV", "供应商");


    static {
    	mappings.put("NONE", NONE);
    	mappings.put("PARTNER", PARTNER);
        mappings.put("ISV", ISV);
        mappings.put("TEMP", TEMP);
    }

    public RemoveBrandUserTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public RemoveBrandUserTypeEnum() {

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

    public static RemoveBrandUserTypeEnum valueof(String code) {
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
        RemoveBrandUserTypeEnum other = (RemoveBrandUserTypeEnum) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
