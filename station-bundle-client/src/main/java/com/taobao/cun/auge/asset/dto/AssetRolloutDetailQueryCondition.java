package com.taobao.cun.auge.asset.dto;

import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.common.PageQuery;

public class AssetRolloutDetailQueryCondition extends PageQuery {

	private static final long serialVersionUID = -4929497832157431915L;
	/**
	 * 出库单id
	 */
	private Long rolloutId;
	
	/**
	 * 出库状态
	 */
	private AssetRolloutIncomeDetailStatusEnum statusEnum;

	public Long getRolloutId() {
		return rolloutId;
	}

	public void setRolloutId(Long rolloutId) {
		this.rolloutId = rolloutId;
	}

	public AssetRolloutIncomeDetailStatusEnum getStatusEnum() {
		return statusEnum;
	}

	public void setStatusEnum(AssetRolloutIncomeDetailStatusEnum statusEnum) {
		this.statusEnum = statusEnum;
	}
}
