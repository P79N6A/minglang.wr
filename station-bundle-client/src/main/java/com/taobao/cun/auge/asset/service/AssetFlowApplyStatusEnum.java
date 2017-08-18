package com.taobao.cun.auge.asset.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @author quanzhu.wangqz
 *
 */
public class AssetFlowApplyStatusEnum implements Serializable{

	private static final long serialVersionUID = -8813269311348834046L;
	
	public static final AssetFlowApplyStatusEnum AUDITING = new AssetFlowApplyStatusEnum("auditing","审核中");
	public static final AssetFlowApplyStatusEnum AUDIT_PASS = new AssetFlowApplyStatusEnum("auditPass","已完成(通过)");
	public static final AssetFlowApplyStatusEnum AUDIT_NOT_PASS = new AssetFlowApplyStatusEnum("auditNotPass","已完成(拒绝)");
	public static final AssetFlowApplyStatusEnum CANCEL = new AssetFlowApplyStatusEnum("cancel","已撤回");
	
    private static final Map<String, AssetFlowApplyStatusEnum> mappings = new HashMap<String, AssetFlowApplyStatusEnum>();
    static {
    	mappings.put("auditing", AUDITING);
    	mappings.put("auditPass", AUDIT_PASS);
    	mappings.put("auditNotPass", AUDIT_NOT_PASS);
    	mappings.put("cancel", CANCEL);
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
        if (!(obj instanceof AssetFlowApplyStatusEnum)) {
            return false;
        }
        AssetFlowApplyStatusEnum objType = (AssetFlowApplyStatusEnum) obj;
        return objType.getCode().equals(this.getCode());
    }


    public static AssetFlowApplyStatusEnum valueof(String code) {
        if (code==null) {
            return null;
        }
        return mappings.get(code);
    }


    public AssetFlowApplyStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
	
	 public AssetFlowApplyStatusEnum() {
   
    }

}
