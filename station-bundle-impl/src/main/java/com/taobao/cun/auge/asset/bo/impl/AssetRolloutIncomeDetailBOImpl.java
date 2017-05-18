package com.taobao.cun.auge.asset.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetailExample.Criteria;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetailExtExample;
import com.taobao.cun.auge.dal.mapper.AssetRolloutIncomeDetailExtMapper;
import com.taobao.cun.auge.dal.mapper.AssetRolloutIncomeDetailMapper;



@Component
public class AssetRolloutIncomeDetailBOImpl implements
		AssetRolloutIncomeDetailBO {
	
	@Autowired
	private  AssetRolloutIncomeDetailMapper  assetRolloutIncomeDetailMapper;
	
	@Autowired
	private AssetRolloutIncomeDetailExtMapper assetRolloutIncomeDetailExtMapper;
	

	public List<AssetCategoryCountDto> queryCountByIncomeId(Long incomeId, String status) {
		ValidateUtils.notNull(incomeId);
		ValidateUtils.notNull(status);
		AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andIncomeIdEqualTo(incomeId).andStatusEqualTo(status);
		//assetRolloutIncomeDetailExtMapper.queryCountGroupByCategory(example);

		return null;
	}

}
