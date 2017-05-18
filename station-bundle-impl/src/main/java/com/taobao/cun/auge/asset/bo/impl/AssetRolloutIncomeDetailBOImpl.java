package com.taobao.cun.auge.asset.bo.impl;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.dal.mapper.AssetRolloutIncomeDetailMapper;

@Component
public class AssetRolloutIncomeDetailBOImpl implements
		AssetRolloutIncomeDetailBO {
	
	@Autowired
	private  AssetRolloutIncomeDetailMapper  assetRolloutIncomeDetailMapper;
	
	
	
	@Override
	public List<AssetCategoryCountDto> queryCountByIncomeId(Long incomeId, String status) {
		// TODO Auto-generated method stub
		return null;
	}

}
