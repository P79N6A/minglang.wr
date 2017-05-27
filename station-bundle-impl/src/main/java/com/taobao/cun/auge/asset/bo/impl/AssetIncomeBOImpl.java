package com.taobao.cun.auge.asset.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.convert.AssetIncomeConverter;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AssetIncome;
import com.taobao.cun.auge.dal.domain.AssetIncomeExample;
import com.taobao.cun.auge.dal.domain.AssetIncomeExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AssetIncomeMapper;

@Component
public class AssetIncomeBOImpl implements AssetIncomeBO {
	
	@Autowired
	private AssetIncomeMapper assetIncomeMapper;
	
	@Autowired
	private AssetRolloutIncomeDetailBO assetRolloutIncomeDetailBO;
	
	@Override
	public Page<AssetIncome> getIncomeList(
			AssetIncomeQueryCondition queryParam) {
		ValidateUtils.notNull(queryParam);
		ValidateUtils.notNull(queryParam.getWorkNo());
		AssetIncomeExample example = new AssetIncomeExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andReceiverWorknoEqualTo(queryParam.getWorkNo());
		PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize());
		return (Page<AssetIncome>)assetIncomeMapper.selectByExample(example); 
	}

	@Override
	public Long addIncome(AssetIncomeDto param) {
		ValidateUtils.notNull(param);
		AssetIncome record = AssetIncomeConverter.toAssetIncome(param);
		DomainUtils.beforeInsert(record, param.getOperator());
		assetIncomeMapper.insert(record);
		return record.getId();
	}

	@Override
	public void updateStatus(Long incomeId, AssetIncomeStatusEnum statusEnum,String operator) {
		ValidateUtils.notNull(incomeId);
		ValidateUtils.notNull(statusEnum);
		ValidateUtils.notNull(operator);
		AssetIncome record = new AssetIncome();
		record.setId(incomeId);
		record.setStatus(statusEnum.getCode());
		DomainUtils.beforeUpdate(record, operator);
		assetIncomeMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public AssetIncome getIncomeById(Long incomeId) {
		ValidateUtils.notNull(incomeId);
		return assetIncomeMapper.selectByPrimaryKey(incomeId);
	}

	@Override
	public AssetIncomeDto getIncomeDtoById(Long incomeId) {
		return AssetIncomeConverter.toAssetIncomeDto(getIncomeById(incomeId));
	}
}
