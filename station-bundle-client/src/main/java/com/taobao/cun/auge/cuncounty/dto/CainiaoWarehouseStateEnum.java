package com.taobao.cun.auge.cuncounty.dto;

import java.util.HashMap;
import java.util.Map;

public class CainiaoWarehouseStateEnum {
	private String code;
    private String desc;
    
    private static final Map<String, CainiaoWarehouseStateEnum> mappings = new HashMap<String, CainiaoWarehouseStateEnum>();

    public static final CainiaoWarehouseStateEnum DELETE = new CainiaoWarehouseStateEnum("DELETE", "已删除");
    public static final CainiaoWarehouseStateEnum USE = new CainiaoWarehouseStateEnum("USE", "已开业");
    public static final CainiaoWarehouseStateEnum CLOSE = new CainiaoWarehouseStateEnum("CLOSE", "终止");
    public static final CainiaoWarehouseStateEnum INIT = new CainiaoWarehouseStateEnum("PLANNING", "待开业");
    
    static {
    	mappings.put("DELETE", DELETE);
    	mappings.put("USE", USE);
    	mappings.put("CLOSE", CLOSE);
    	mappings.put("INIT", INIT);
    }
    
    public CainiaoWarehouseStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
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

	public static CainiaoWarehouseStateEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return mappings.get(code);
    }
}
