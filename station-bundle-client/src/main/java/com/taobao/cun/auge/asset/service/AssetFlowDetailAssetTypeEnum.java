package com.taobao.cun.auge.asset.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @author quanzhu.wangqz
 *
 */
public class AssetFlowDetailAssetTypeEnum implements Serializable{

	private static final long serialVersionUID = -8813269311348834046L;
	
	public static final AssetFlowDetailAssetTypeEnum IT_ASSET = new AssetFlowDetailAssetTypeEnum("it","IT资产");
	public static final AssetFlowDetailAssetTypeEnum ADMIN_ASSET = new AssetFlowDetailAssetTypeEnum("admin","行政资产");
	
    private static final Map<String, AssetFlowDetailAssetTypeEnum> mappings = new HashMap<String, AssetFlowDetailAssetTypeEnum>();
    static {
    	mappings.put("it", IT_ASSET);
    	mappings.put("admin", ADMIN_ASSET);
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
        if (obj == null)
            return false;
        if (!(obj instanceof AssetFlowDetailAssetTypeEnum))
            return false;
        AssetFlowDetailAssetTypeEnum objType = (AssetFlowDetailAssetTypeEnum) obj;
        return objType.getCode().equals(this.getCode());
    }


    public static AssetFlowDetailAssetTypeEnum valueof(String code) {
        if (code==null)
            return null;
        return mappings.get(code);
    }


    public AssetFlowDetailAssetTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
