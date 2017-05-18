package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCountDto;

public interface AssetRolloutIncomeDetailExtMapper {
    
	public List<AssetCountDto> queryCountByIncomeId(Long incomeId, String status);
}