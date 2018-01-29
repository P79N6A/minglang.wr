package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StationModifyApplyBusitypeEnum implements Serializable{
	 private static final long serialVersionUID = 1409002910681385324L;

	    public static final StationModifyApplyBusitypeEnum NAME_MODIFY = new StationModifyApplyBusitypeEnum("NAME_MODIFY","村点名称修改");

	    private static final Map<String, StationModifyApplyBusitypeEnum> mappings = new HashMap<String, StationModifyApplyBusitypeEnum>();
	    static {
	        mappings.put("NAME_MODIFY", NAME_MODIFY);
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
	        if (!(obj instanceof StationModifyApplyBusitypeEnum)) {
	            return false;
	        }
	        StationModifyApplyBusitypeEnum objType = (StationModifyApplyBusitypeEnum) obj;
	        return objType.getCode().equals(this.getCode());
	    }


	    public static StationModifyApplyBusitypeEnum valueof(String code) {
	        if (code==null) {
	            return null;
	        }
	        return mappings.get(code);
	    }


	    public StationModifyApplyBusitypeEnum(String code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }

	    public StationModifyApplyBusitypeEnum() {

	    }
}
