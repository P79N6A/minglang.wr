package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StationModifyApplyStatusEnum  implements Serializable{

	 private static final long serialVersionUID = 1409002910681385324L;

	    public static final StationModifyApplyStatusEnum AUDITING = new StationModifyApplyStatusEnum("auditing","审核中");
	    public static final StationModifyApplyStatusEnum AUDIT_PASS = new StationModifyApplyStatusEnum("auditPass","已完成(通过)");
	    public static final StationModifyApplyStatusEnum AUDIT_NOT_PASS = new StationModifyApplyStatusEnum("auditNotPass","已完成(拒绝)");

	    private static final Map<String, StationModifyApplyStatusEnum> mappings = new HashMap<String, StationModifyApplyStatusEnum>();
	    static {
	        mappings.put("auditing", AUDITING);
	        mappings.put("auditPass", AUDIT_PASS);
	        mappings.put("auditNotPass", AUDIT_NOT_PASS);
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
	        if (!(obj instanceof StationModifyApplyStatusEnum)) {
	            return false;
	        }
	        StationModifyApplyStatusEnum objType = (StationModifyApplyStatusEnum) obj;
	        return objType.getCode().equals(this.getCode());
	    }


	    public static StationModifyApplyStatusEnum valueof(String code) {
	        if (code==null) {
	            return null;
	        }
	        return mappings.get(code);
	    }


	    public StationModifyApplyStatusEnum(String code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }

	    public StationModifyApplyStatusEnum() {

	    }
}
