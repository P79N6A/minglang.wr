package com.taobao.cun.auge.asset.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AssetIncome;
import com.taobao.cun.auge.dal.domain.AssetIncomeExample;
import com.taobao.cun.auge.dal.domain.AssetIncomeExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AssetIncomeMapper;

@Component
public class AssetIncomeBOImpl implements AssetIncomeBO {
	
	@Autowired
	private AssetIncomeMapper assetIncomeMapper;
	
	@Override
	public Page<AssetIncome> getIncomeList(
			AssetIncomeQueryCondition queryParam) {
		ValidateUtils.notNull(queryParam);
		ValidateUtils.notNull(queryParam.getWorkNo());
		AssetIncomeExample example = new AssetIncomeExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andReceiverWorknoEqualTo(queryParam.getWorkNo());
		return (Page<AssetIncome>)assetIncomeMapper.selectByExample(example); 
	}

}
