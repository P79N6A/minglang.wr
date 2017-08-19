package com.taobao.cun.auge.asset.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @author quanzhu.wangqz
 *
 */
public class AssetFlowPurchaseStatusEnum implements Serializable{

	private static final long serialVersionUID = -8813269311348834046L;
	
	public static final AssetFlowPurchaseStatusEnum HAS_ORDER = new AssetFlowPurchaseStatusEnum("hasOrder","已下单");
	public static final AssetFlowPurchaseStatusEnum NO_ORDER = new AssetFlowPurchaseStatusEnum("noOrder","未下单");

    private static final Map<String, AssetFlowPurchaseStatusEnum> mappings = new HashMap<String, AssetFlowPurchaseStatusEnum>();
    static {
        mappings.put("hasOrder", HAS_ORDER);
        mappings.put("noOrder", NO_ORDER);
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
        if (!(obj instanceof AssetFlowPurchaseStatusEnum)) {
            return false;
        }
        AssetFlowPurchaseStatusEnum objType = (AssetFlowPurchaseStatusEnum) obj;
        return objType.getCode().equals(this.getCode());
    }


    public static AssetFlowPurchaseStatusEnum valueof(String code) {
        if (code==null) {
            return null;
        }
        return mappings.get(code);
    }


    public AssetFlowPurchaseStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
