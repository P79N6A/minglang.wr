package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;
/**
 * 资产出库单撤销参数
 * @author quanzhu.wangqz
 *
 */
public class AssetRolloutCancelDto extends OperatorDto implements Serializable{

    private static final long serialVersionUID = 8306426246791798460L;
    /**
     * 出库单id
     */
    private Long rolloutId;

	public Long getRolloutId() {
		return rolloutId;
	}

	public void setRolloutId(Long rolloutId) {
		this.rolloutId = rolloutId;
	}
    
}
