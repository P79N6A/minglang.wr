package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;

public interface AssetRolloutIncomeDetailBO {

	/**
	 * 入库单id  和状态查资产总数
	 * @param incomeId
	 * @param status
	 * @return
	 */
	public List<AssetCategoryCountDto> queryCountByIncomeId(Long incomeId, AssetRolloutIncomeDetailStatusEnum status);
	
	/**
	 * 出库单id  和状态查资产总数
	 * @param rolloutId
	 * @param status
	 * @return
	 */
	public List<AssetCategoryCountDto> queryCountByRolloutId(Long AssetRolloutIncomeDetailStatusEnum, AssetRolloutIncomeDetailStatusEnum status);
}
