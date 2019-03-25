package com.taobao.cun.auge.cuncounty.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 县服务中心状态
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyStateEnum implements Serializable {
	private static final long serialVersionUID = 4165208660523686627L;

	private static final Map<String, CuntaoCountyStateEnum> mappings = new HashMap<String, CuntaoCountyStateEnum>();

    private String code;
    private String desc;

	public static final CuntaoCountyStateEnum PLANNING = new CuntaoCountyStateEnum("PLANNING", "筹划中");
	public static final CuntaoCountyStateEnum WAIT_OPEN_AUDIT = new CuntaoCountyStateEnum("WAIT_OPEN_AUDIT", "待开业审批");
	public static final CuntaoCountyStateEnum WAIT_OPEN_AUDIT_FAIL = new CuntaoCountyStateEnum("WAIT_OPEN_AUDIT_FAIL", "待开业审批失败");
	public static final CuntaoCountyStateEnum WAIT_OPEN = new CuntaoCountyStateEnum("WAIT_OPEN", "待开业");
	public static final CuntaoCountyStateEnum OPENING = new CuntaoCountyStateEnum("OPENING", "已开业");

    static {
    	mappings.put("PLANNING", PLANNING);
		mappings.put("WAIT_OPEN_AUDIT", WAIT_OPEN_AUDIT);
		mappings.put("WAIT_OPEN_AUDIT_FAIL", WAIT_OPEN_AUDIT_FAIL);
		mappings.put("WAIT_OPEN", WAIT_OPEN);
		mappings.put("OPENING", OPENING);

    }

    public CuntaoCountyStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public CuntaoCountyStateEnum() {

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

    public static CuntaoCountyStateEnum valueof(String code) {
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
        CuntaoCountyStateEnum other = (CuntaoCountyStateEnum) obj;
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
