package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人实例表，是否当前合伙人 枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceIsCurrentEnum implements Serializable {

    private static final Map<String, PartnerInstanceIsCurrentEnum> mappings = new HashMap<String, PartnerInstanceIsCurrentEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;

    public static final PartnerInstanceIsCurrentEnum Y = new PartnerInstanceIsCurrentEnum(
			"y", "是");

	public static final PartnerInstanceIsCurrentEnum N = new PartnerInstanceIsCurrentEnum(
			"n", "否");
	


    static {
        mappings.put("y", Y);
        mappings.put("n", N);
    }

    public PartnerInstanceIsCurrentEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public PartnerInstanceIsCurrentEnum() {

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

    public static PartnerInstanceIsCurrentEnum valueof(String code) {
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
        PartnerInstanceIsCurrentEnum other = (PartnerInstanceIsCurrentEnum) obj;
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
