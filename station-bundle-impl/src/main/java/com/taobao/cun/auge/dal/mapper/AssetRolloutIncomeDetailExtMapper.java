package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;

public interface AssetRolloutIncomeDetailExtMapper {
    
	public List<AssetCategoryCountDto> queryCountByIncomeId(Long incomeId, String status);
}