package com.taobao.cun.auge.asset.bo.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.convert.AssetRolloutIncomeDetailConverter;
import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetailExample.Criteria;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetailExtExample;
import com.taobao.cun.auge.dal.mapper.AssetRolloutIncomeDetailExtMapper;
import com.taobao.cun.auge.dal.mapper.AssetRolloutIncomeDetailMapper;
import com.taobao.cun.auge.station.exception.AugeBusinessException;



@Component
public class AssetRolloutIncomeDetailBOImpl implements
		AssetRolloutIncomeDetailBO {
	
	@Autowired
	private  AssetRolloutIncomeDetailMapper  assetRolloutIncomeDetailMapper;
	
	@Autowired
	private AssetRolloutIncomeDetailExtMapper assetRolloutIncomeDetailExtMapper;
	
	@Autowired
	private AssetRolloutBO assetRolloutBO;
	
	@Autowired
	private AssetIncomeBO assetIncomeBO;
	

	public List<AssetCategoryCountDto> queryCountByIncomeId(Long incomeId, AssetRolloutIncomeDetailStatusEnum status) {
		ValidateUtils.notNull(incomeId);
		//ValidateUtils.notNull(status);
		AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andIncomeIdEqualTo(incomeId);
		if (status != null) {
			criteria.andStatusEqualTo(status.getCode());
		}
		return assetRolloutIncomeDetailExtMapper.queryCountGroupByCategory(example);
	}


	@Override
	public List<AssetCategoryCountDto> queryCountByRolloutId(Long rolloutId,
			AssetRolloutIncomeDetailStatusEnum status) {
		ValidateUtils.notNull(rolloutId);
		ValidateUtils.notNull(status);
		AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andRolloutIdEqualTo(rolloutId).andStatusEqualTo(status.getCode());
		return assetRolloutIncomeDetailExtMapper.queryCountGroupByCategory(example);
	}

	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void signAsset(Long id,String operator) {
		ValidateUtils.notNull(id);
		AssetRolloutIncomeDetail record = new AssetRolloutIncomeDetail();
		record.setId(id);
		record.setStatus(AssetRolloutIncomeDetailStatusEnum.HAS_SIGN.getCode());
		DomainUtils.beforeUpdate(record, operator);
		assetRolloutIncomeDetailMapper.updateByPrimaryKeySelective(record);
	}


	@Override
	public Boolean isAllSignByRolloutId(Long rolloutId) {
		AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
		criteria.andRolloutIdEqualTo(rolloutId);
		List<AssetRolloutIncomeDetail>  resList = assetRolloutIncomeDetailMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(resList)) {
			return true;
		}
		return false;
	}


	@Override
	public Boolean isAllSignByIncomeId(Long incomeId) {
		AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
		criteria.andIncomeIdEqualTo(incomeId);
		List<AssetRolloutIncomeDetail>  resList = assetRolloutIncomeDetailMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(resList)) {
			return true;
		}
		return false;
	}


	@Override
	public Long addDetail(AssetRolloutIncomeDetailDto param) {
		ValidateUtils.notNull(param);
		AssetRolloutIncomeDetail record = AssetRolloutIncomeDetailConverter.toAssetRolloutIncomeDetail(param);
		DomainUtils.beforeInsert(record, param.getOperator());
		assetRolloutIncomeDetailMapper.insert(record);
		return record.getId();
	}


	@Override
	public List<AssetRolloutIncomeDetail> queryListByRolloutId(Long rolloutId) {
		AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andRolloutIdEqualTo(rolloutId);
		return assetRolloutIncomeDetailMapper.selectByExample(example);
	}


	@Override
	public AssetRolloutIncomeDetail queryWaitSignByAssetId(Long assetId) {
		ValidateUtils.notNull(assetId);
		AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
		criteria.andAssetIdEqualTo(assetId);
		List<AssetRolloutIncomeDetail>  resList = assetRolloutIncomeDetailMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(resList)) {
			 throw new AugeBusinessException("操作失败，无法查询到待签收资产，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		if (resList.size()>1) {
			 throw new AugeBusinessException("操作失败，当前资产多条数据，请核对资产信息！如有疑问，请联系资产管理员。");
		}
	    return resList.get(0);
	}


	@Override
	public void cancel(Long rolloutId, String operator) {
		ValidateUtils.notNull(rolloutId);
		AssetRolloutIncomeDetail record = new AssetRolloutIncomeDetail();
		record.setStatus(AssetRolloutIncomeDetailStatusEnum.CANCEL.getCode());
		DomainUtils.beforeUpdate(record, operator);
		
		AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andRolloutIdEqualTo(rolloutId);
		assetRolloutIncomeDetailMapper.updateByExampleSelective(record, example);
		
	}
}
