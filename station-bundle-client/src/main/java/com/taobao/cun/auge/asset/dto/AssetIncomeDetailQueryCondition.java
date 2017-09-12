package com.taobao.cun.auge.asset.dto;

import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.common.PageQuery;

public class AssetIncomeDetailQueryCondition extends PageQuery {

	private static final long serialVersionUID = -4929497832157431915L;
	/**
	 * 入库单id
	 */
	private Long incomeId;
	
	/**
	 * 入库状态
	 */
	private AssetRolloutIncomeDetailStatusEnum statusEnum;

	public Long getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(Long incomeId) {
		this.incomeId = incomeId;
	}

	public AssetRolloutIncomeDetailStatusEnum getStatusEnum() {
		return statusEnum;
	}

	public void setStatusEnum(AssetRolloutIncomeDetailStatusEnum statusEnum) {
		this.statusEnum = statusEnum;
	}
}
