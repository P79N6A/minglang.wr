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
	public void signAsset(Long assetId,
			AssetRolloutIncomeDetailTypeEnum typeEnum,String operator) {
		ValidateUtils.notNull(assetId);
		ValidateUtils.notNull(typeEnum);
		
		AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
		criteria.andTypeEqualTo(typeEnum.getCode());
		List<AssetRolloutIncomeDetail>  resList = assetRolloutIncomeDetailMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(resList)) {
			 throw new AugeBusinessException("操作失败，无法查询到待签收资产，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		if (resList.size()>1) {
			 throw new AugeBusinessException("操作失败，当前资产多条数据，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		AssetRolloutIncomeDetail detail = resList.get(0);
		
		//更新出入库单状态
		Long incomeId = detail.getIncomeId();
		Long rolloutId =detail.getRolloutId();
		if (incomeId != null) {
			if (isAllSignByIncomeId(incomeId)) {
				assetIncomeBO.updateStatus(incomeId, AssetIncomeStatusEnum.DONE, operator);
			}else {
				assetIncomeBO.updateStatus(incomeId, AssetIncomeStatusEnum.DOING, operator);
			}
		}
		if (rolloutId != null) {
			if (isAllSignByRolloutId(rolloutId)) {
				assetRolloutBO.updateStatus(incomeId, AssetRolloutStatusEnum.ROLLOUT_DONE, operator);
			}else {
				assetRolloutBO.updateStatus(incomeId, AssetRolloutStatusEnum.ROLLOUT_ING, operator);
			}
		}
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
}
