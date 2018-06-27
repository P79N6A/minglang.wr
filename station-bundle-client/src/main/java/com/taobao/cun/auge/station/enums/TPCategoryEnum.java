package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TPCategoryEnum implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7188080636536035037L;

	
	
	  private static final Map<String, TPCategoryEnum> mappings = new HashMap<String, TPCategoryEnum>();



	    private String code;
	    private String desc;

	    public static final TPCategoryEnum ELEC = new TPCategoryEnum("ELEC", "家电");
	   

	    static {
	        mappings.put("ELEC", ELEC);
	    }

	    public TPCategoryEnum(String code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }

	    public TPCategoryEnum() {

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

	    public static TPCategoryEnum valueof(String code) {
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
	        TPCategoryEnum other = (TPCategoryEnum) obj;
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
