package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人表 经营类型 枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerBusinessTypeEnum implements Serializable {

    private static final Map<String, PartnerBusinessTypeEnum> mappings = new HashMap<String, PartnerBusinessTypeEnum>();


    private static final long serialVersionUID = -2325045809951918493L;

    private String code;
    private String desc;

    public static final PartnerBusinessTypeEnum PARTTIME = new PartnerBusinessTypeEnum(
			"PARTTIME", "兼职");

	public static final PartnerBusinessTypeEnum FULLTIME = new PartnerBusinessTypeEnum(
			"FULLTIME", "全职");
	


    static {
        mappings.put("PARTTIME", PARTTIME);
        mappings.put("FULLTIME", FULLTIME);
    }

    public PartnerBusinessTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public PartnerBusinessTypeEnum() {

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

    public static PartnerBusinessTypeEnum valueof(String code) {
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
        PartnerBusinessTypeEnum other = (PartnerBusinessTypeEnum) obj;
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
