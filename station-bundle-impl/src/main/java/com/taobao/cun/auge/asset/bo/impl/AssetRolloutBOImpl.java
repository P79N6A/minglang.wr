package com.taobao.cun.auge.asset.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.convert.AssetRolloutConverter;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutQueryCondition;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AssetIncome;
import com.taobao.cun.auge.dal.domain.AssetRollout;
import com.taobao.cun.auge.dal.domain.AssetRolloutExample;
import com.taobao.cun.auge.dal.domain.AssetRolloutExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AssetRolloutMapper;

@Component
public class AssetRolloutBOImpl implements AssetRolloutBO {
	
	@Autowired
	private AssetRolloutMapper assetRolloutMapper;
	
	@Override
	public Page<AssetRollout> getRolloutList(
			AssetRolloutQueryCondition queryParam) {
		ValidateUtils.notNull(queryParam);
		ValidateUtils.notNull(queryParam.getWorkNo());
		AssetRolloutExample example = new AssetRolloutExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n").andApplierWorknoEqualTo(queryParam.getWorkNo());
		PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize());
		return (Page<AssetRollout>)assetRolloutMapper.selectByExample(example); 
	}

	@Override
	public Long addRollout(AssetRolloutDto param) {
		ValidateUtils.notNull(param);
		AssetRollout record = AssetRolloutConverter.toAssetRollout(param);
		DomainUtils.beforeInsert(record, param.getOperator());
		assetRolloutMapper.insert(record);
		return record.getId();
	}

	@Override
	public void cancelRolleout(Long rolloutId, OperatorDto operatorDto) {
		// setp1 撤销出库单
	   /**
	    * 撤销 详情表 为取消
	    * 资产状态 改成使用中
	    * 撤销工作流
	    */
		
	}

	@Override
	public AssetRollout getRolloutById(Long rolloutId) {
		ValidateUtils.notNull(rolloutId);
		return assetRolloutMapper.selectByPrimaryKey(rolloutId);
	}

	@Override
	public AssetRolloutDto getRolloutDtoById(Long rolloutId) {
		return AssetRolloutConverter.toAssetRolloutDto(getRolloutById(rolloutId));
	}

	public void updateStatus(Long rolloutId, AssetRolloutStatusEnum statusEnum,String operator) {
		ValidateUtils.notNull(rolloutId);
		ValidateUtils.notNull(statusEnum);
		ValidateUtils.notNull(operator);
		AssetRollout record = new AssetRollout();
		record.setId(rolloutId);
		record.setStatus(statusEnum.getCode());
		DomainUtils.beforeUpdate(record, operator);
		assetRolloutMapper.updateByPrimaryKeySelective(record);
	}


}
