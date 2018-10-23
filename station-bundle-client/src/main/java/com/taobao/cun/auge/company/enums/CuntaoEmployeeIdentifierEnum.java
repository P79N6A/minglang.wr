package com.taobao.cun.auge.company.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * 身份
 *
 */
public class CuntaoEmployeeIdentifierEnum implements Serializable{
private static final long serialVersionUID = 1173751851665155410L;
	
	private String code;
	private String desc;
	
	public static final CuntaoEmployeeIdentifierEnum VENDOR_DISTRIBUTOR = new CuntaoEmployeeIdentifierEnum("VENDOR_DISTRIBUTOR","配送员");
	public static final CuntaoEmployeeIdentifierEnum STORE_PICKER = new CuntaoEmployeeIdentifierEnum("STORE_PICKER","拣货员");
	public static final CuntaoEmployeeIdentifierEnum VENDOR_INSTALLER = new CuntaoEmployeeIdentifierEnum("VENDOR_INSTALLER","安装员");
	public static final CuntaoEmployeeIdentifierEnum VENDOR_MANAGER = new CuntaoEmployeeIdentifierEnum("VENDOR_MANAGER","供应商管理员");
	public static final CuntaoEmployeeIdentifierEnum STORE_MANAGER = new CuntaoEmployeeIdentifierEnum("STORE_MANAGER","门店管理员");


	public static final Map<String, CuntaoEmployeeIdentifierEnum> mappings = new HashMap<String, CuntaoEmployeeIdentifierEnum>();
	
	static {
		mappings.put("VENDOR_DISTRIBUTOR", VENDOR_DISTRIBUTOR);
		mappings.put("STORE_PICKER", STORE_PICKER);
		mappings.put("VENDOR_INSTALLER", VENDOR_INSTALLER);
		mappings.put("VENDOR_MANAGER", VENDOR_MANAGER);
		mappings.put("STORE_MANAGER", STORE_MANAGER);
		
	}

	private CuntaoEmployeeIdentifierEnum() {}
	
	private CuntaoEmployeeIdentifierEnum(String code, String desc) {
		this.code = code;
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
		if (!(obj instanceof CuntaoEmployeeIdentifierEnum)) {
            return false;
        }
		CuntaoEmployeeIdentifierEnum objType = (CuntaoEmployeeIdentifierEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public static CuntaoEmployeeIdentifierEnum valueof(String code) {
		if (code==null) {
            return null;
        }
		return mappings.get(code);
	}

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
}
