package com.taobao.cun.auge.asset.bo.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.convert.AssetIncomeConverter;
import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.AssetIncome;
import com.taobao.cun.auge.dal.domain.AssetIncomeExample;
import com.taobao.cun.auge.dal.domain.AssetIncomeExample.Criteria;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.dal.mapper.AssetIncomeMapper;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

@Component
public class AssetIncomeBOImpl implements AssetIncomeBO {
	
	@Autowired
	private AssetIncomeMapper assetIncomeMapper;
	@Autowired
	private AssetRolloutIncomeDetailBO assetRolloutIncomeDetailBO;
	@Autowired
	private AssetRolloutBO assetRolloutBO;
	@Autowired
	private DiamondConfiguredProperties configuredProperties;
	
	@Override
	public PageDto<AssetIncomeDto> getIncomeList(AssetIncomeQueryCondition queryParam) {
		ValidateUtils.notNull(queryParam);
		ValidateUtils.notNull(queryParam.getWorkNo());
		AssetIncomeExample example = new AssetIncomeExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andReceiverWorknoEqualTo(queryParam.getWorkNo());
		if (StringUtils.isNotEmpty(queryParam.getStatus())) {
			criteria.andStatusEqualTo(queryParam.getStatus());
		}
		if (StringUtils.isNotEmpty(queryParam.getType())) {
			criteria.andTypeEqualTo(queryParam.getType());
		}
		example.setOrderByClause("GMT_CREATE DESC");
		PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize());
		Page<AssetIncome> incomeList = (Page<AssetIncome>)assetIncomeMapper.selectByExample(example);
		
		List<AssetIncomeDto> dtoList = new ArrayList<AssetIncomeDto>();
        for (AssetIncome ai : incomeList) {
            AssetIncomeDto aiDto = AssetIncomeConverter.toAssetIncomeDto(ai);
            bulidCount(aiDto);
        }
        return PageDtoUtil.success(incomeList, dtoList);
	}
	
	private void bulidCount(AssetIncomeDto aiDto) {
		List<String> count = new ArrayList<String>();
	    count.add(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
	    count.add(AssetRolloutIncomeDetailStatusEnum.HAS_SIGN.getCode());
	    List<AssetCategoryCountDto> res = assetRolloutIncomeDetailBO.queryCountByIncomeId(aiDto.getId(), count);
	    res.forEach(n -> n.setCategoryName(configuredProperties.getCategoryMap().get(n.getCategory())));
	    aiDto.setCountList(res);
	    List<String> waitsign = new ArrayList<String>();
	    waitsign.add(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
	    List<AssetCategoryCountDto> res1 = assetRolloutIncomeDetailBO.queryCountByIncomeId(aiDto.getId(),waitsign);
		res1.forEach(n -> n.setCategoryName(configuredProperties.getCategoryMap().get(n.getCategory())));
		aiDto.setWaitSignCountList(res1);
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
		AssetIncomeDto dto = AssetIncomeConverter.toAssetIncomeDto(getIncomeById(incomeId));
		bulidCount(dto);
		return dto;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void signAsset(Long assetId,String operator) {
		ValidateUtils.notNull(assetId);
		ValidateUtils.notNull(operator);
		
		AssetRolloutIncomeDetail detail = assetRolloutIncomeDetailBO.queryWaitSignByAssetId(assetId);
		Long incomeId = detail.getIncomeId();
		if (incomeId== null) {
			throw new AugeBusinessException("入库失败，待签收资产没有对应的入库单，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		AssetIncome ai = getIncomeById(incomeId);
		if (!ai.getReceiverWorkno().equals(operator)) {
			throw new AugeBusinessException("入库失败，该资产不属于您，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		//签收资产
		assetRolloutIncomeDetailBO.signAsset(detail.getId(), operator);
		//更新出入库单状态
		Long rolloutId =detail.getRolloutId();
		if (assetRolloutIncomeDetailBO.isAllSignByIncomeId(incomeId)) {
			updateStatus(incomeId, AssetIncomeStatusEnum.DONE, operator);
			if (rolloutId != null) {
				assetRolloutBO.updateStatus(rolloutId, AssetRolloutStatusEnum.ROLLOUT_DONE, operator);
			}
		}else {
			updateStatus(incomeId, AssetIncomeStatusEnum.DOING, operator);
			if (rolloutId != null) {
				assetRolloutBO.updateStatus(rolloutId, AssetRolloutStatusEnum.ROLLOUT_ING, operator);
			}
		}
	}

	@Override
	public void cancelAssetIncome(Long incomeId, String operator) {
		ValidateUtils.notNull(incomeId);
		AssetIncome record = new AssetIncome();
		record.setId(incomeId);
		record.setStatus(AssetIncomeStatusEnum.CANCEL.getCode());
		DomainUtils.beforeUpdate(record, operator);
		assetIncomeMapper.updateByPrimaryKeySelective(record);
		
	}
}
