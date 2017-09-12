package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.common.OperatorDto;


public class AssetCheckDto extends OperatorDto implements Serializable{

    private static final long serialVersionUID = 8306426246791798460L;
    
    /**
     * 使用人id：工号，taobaouserid
     */
    private String userId;
    /**
     * 使用地区：县服务中心，服务站
     */
    private AssetUseAreaTypeEnum useAreaType;
    /**
     * 资产编号
     */
    private String aliNo;
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public AssetUseAreaTypeEnum getUseAreaType() {
		return useAreaType;
	}
	public void setUseAreaType(AssetUseAreaTypeEnum useAreaType) {
		this.useAreaType = useAreaType;
	}
	public String getAliNo() {
		return aliNo;
	}
	public void setAliNo(String aliNo) {
		this.aliNo = aliNo;
	}
}
