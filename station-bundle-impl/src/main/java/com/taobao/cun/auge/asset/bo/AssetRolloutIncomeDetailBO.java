package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCountDto;

public interface AssetRolloutIncomeDetailBO {

	/**
	 * 入库单id  和状态查资产总数
	 * @param incomeId
	 * @param status
	 * @return
	 */
	public List<AssetCountDto> queryCountByIncomeId(Long incomeId,String status);
}
