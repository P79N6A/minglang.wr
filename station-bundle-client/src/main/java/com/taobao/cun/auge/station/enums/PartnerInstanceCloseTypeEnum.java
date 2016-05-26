package com.taobao.cun.auge.station.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人实例 退出类型枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceCloseTypeEnum {

	 private static final Map<String, PartnerInstanceCloseTypeEnum> mappings = new HashMap<String, PartnerInstanceCloseTypeEnum>();


	    private static final long serialVersionUID = -2325045809951918493L;

	    private String code;
	    private String desc;

	    public static final PartnerInstanceCloseTypeEnum Y = new PartnerInstanceCloseTypeEnum(
				"y", "是");

		public static final PartnerInstanceCloseTypeEnum N = new PartnerInstanceCloseTypeEnum(
				"n", "否");
		


	    static {
	        mappings.put("y", Y);
	        mappings.put("n", N);
	    }

	    public PartnerInstanceCloseTypeEnum(String code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }

	    public PartnerInstanceCloseTypeEnum() {

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

	    public static PartnerInstanceCloseTypeEnum valueof(String code) {
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
	        PartnerInstanceCloseTypeEnum other = (PartnerInstanceCloseTypeEnum) obj;
	        if (code == null) {
	            if (other.code != null)
	                return false;
	        } else if (!code.equals(other.code))
	            return false;
	        return true;
	    }
}
