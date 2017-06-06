package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;
/**
 * 资产出库撤销参数
 * @author quanzhu.wangqz
 *
 */
public class AssetRolloutCancelDto extends OperatorDto implements Serializable{

    private static final long serialVersionUID = 8306426246791798460L;
    /**
     * 资产id
     */
    private Long assetId;
    
	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
}
