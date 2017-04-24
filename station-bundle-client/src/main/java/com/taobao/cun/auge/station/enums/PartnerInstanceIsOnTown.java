package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author alibaba-54766
 * 是否在镇上枚举
 */
public class PartnerInstanceIsOnTown implements Serializable{

	private static final long serialVersionUID = -193289689209234713L;

	private static final Map<String, PartnerInstanceIsOnTown> mappings = new HashMap<String, PartnerInstanceIsOnTown>();
	
	private String code;
    private String desc;

    public static final PartnerInstanceIsOnTown Y = new PartnerInstanceIsOnTown(
			"y", "是");

	public static final PartnerInstanceIsOnTown N = new PartnerInstanceIsOnTown(
			"n", "否");
	


    static {
        mappings.put("y", Y);
        mappings.put("n", N);
    }

    public PartnerInstanceIsOnTown(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public PartnerInstanceIsOnTown() {

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

    public static PartnerInstanceIsOnTown valueof(String code) {
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
        PartnerInstanceIsOnTown other = (PartnerInstanceIsOnTown) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
}
