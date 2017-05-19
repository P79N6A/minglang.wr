package com.taobao.cun.auge.dal.mapper;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetailExtExample;

public interface AssetRolloutIncomeDetailExtMapper {
    
	public List<AssetCategoryCountDto> queryCountGroupByCategory(AssetRolloutIncomeDetailExtExample extExample);
}