package com.taobao.cun.auge.asset.bo.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.convert.AssetIncomeConverter;
import com.taobao.cun.auge.asset.dto.AssetAppMessageDto;
import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetSignDto;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutTypeEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetIncome;
import com.taobao.cun.auge.dal.domain.AssetIncomeExample;
import com.taobao.cun.auge.dal.domain.AssetIncomeExample.Criteria;
import com.taobao.cun.auge.dal.domain.AssetRollout;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.dal.mapper.AssetIncomeMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	@Autowired
	private AssetBO assetBO;
    @Autowired
    private Emp360Adapter emp360Adapter;
    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
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
		if (queryParam.getOperatorOrgId() != null) {
			criteria.andReceiverOrgIdEqualTo(queryParam.getOperatorOrgId());
		}
		example.setOrderByClause("GMT_CREATE DESC");
		PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize());
		Page<AssetIncome> incomeList = (Page<AssetIncome>)assetIncomeMapper.selectByExample(example);
		
		List<AssetIncomeDto> dtoList = new ArrayList<AssetIncomeDto>();
        for (AssetIncome ai : incomeList) {
            AssetIncomeDto aiDto = AssetIncomeConverter.toAssetIncomeDto(ai);
            bulidCount(aiDto);
            dtoList.add(aiDto);
            
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
	public void signAssetByCounty(String aliNo,String operator,Long operatorOrgId) {

		ValidateUtils.notNull(aliNo);
		ValidateUtils.notNull(operator);
		Asset asset = assetBO.getAssetByAliNo(aliNo);
		if (asset == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "入库失败" + AssetBO.NO_EXIT_ASSET);
		}
		AssetRolloutIncomeDetail detail = assetRolloutIncomeDetailBO.queryWaitSignByAssetId(asset.getId());
		if (detail == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"入库失败，当前资产不是待签收资产，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		Long incomeId = detail.getIncomeId();
		if (incomeId== null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"入库失败，待签收资产没有对应的入库单，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		AssetIncome ai = getIncomeById(incomeId);
		if (!ai.getReceiverWorkno().equals(operator)) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"入库失败，该资产不属于您，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		
		if (!ai.getReceiverOrgId().equals(operatorOrgId)) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"入库失败，接收地点与当前组织不匹配，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		//签收资产
		assetRolloutIncomeDetailBO.signAsset(detail.getId(), operator);
		//更新出入库单状态
		Long rolloutId =detail.getRolloutId();
		if (assetRolloutIncomeDetailBO.isAllSignByIncomeId(incomeId)) {
			updateStatus(incomeId, AssetIncomeStatusEnum.DONE, operator);
			if (rolloutId != null) {
				assetRolloutBO.updateStatus(rolloutId, AssetRolloutStatusEnum.ROLLOUT_DONE, operator);
				AssetRollout ar = assetRolloutBO.getRolloutById(rolloutId);
				if (AssetRolloutTypeEnum.TRANSFER.getCode().equals(ar.getType())) {
					AssetAppMessageDto msgDto = new AssetAppMessageDto();
					msgDto.setMsgTypeDetail("SIGN");
					msgDto.setBizId(rolloutId);
					msgDto.setTitle("您转移的资产已被对方签收，请关注!");
					msgDto.setContent("您转移至" + ar.getReceiverAreaName() + " " +ar.getReceiverName() + "的资产已被对方签收，查看详情");
					List<Long>  rList = new ArrayList<Long>();
					rList.add(Long.parseLong(ar.getApplierWorkno()));
					msgDto.setReceiverList(rList);
					assetBO.sendAppMessage(msgDto);
				}
				
			}
		}else {
			updateStatus(incomeId, AssetIncomeStatusEnum.DOING, operator);
			if (rolloutId != null) {
				assetRolloutBO.updateStatus(rolloutId, AssetRolloutStatusEnum.ROLLOUT_ING, operator);
			}
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void signAssetByStation(Long assetId, String operator) {
		ValidateUtils.notNull(assetId);
		ValidateUtils.notNull(operator);
		
		AssetRolloutIncomeDetail detail = assetRolloutIncomeDetailBO.queryWaitSignByAssetId(assetId);
		if (detail == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"签收失败，当前资产不是待签收资产，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		Long rolloutId = detail.getRolloutId();
		if (rolloutId== null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"签收失败，待签收资产没有对应的出库单，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		AssetRollout ar = assetRolloutBO.getRolloutById(rolloutId);
		if (!ar.getReceiverId().equals(operator)) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"签收失败，该资产不属于您，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		//签收资产
		assetRolloutIncomeDetailBO.signAsset(detail.getId(), operator);
		//更新出入库单状态
		if (assetRolloutIncomeDetailBO.isAllSignByRolloutId(rolloutId)) {
			assetRolloutBO.updateStatus(rolloutId, AssetRolloutStatusEnum.ROLLOUT_DONE, operator);
		}else {
			assetRolloutBO.updateStatus(rolloutId, AssetRolloutStatusEnum.ROLLOUT_ING, operator);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void cancelAssetIncome(Long incomeId, String operator) {
		ValidateUtils.notNull(incomeId);
		AssetIncome record = new AssetIncome();
		record.setId(incomeId);
		record.setStatus(AssetIncomeStatusEnum.CANCEL.getCode());
		DomainUtils.beforeUpdate(record, operator);
		assetIncomeMapper.updateByPrimaryKeySelective(record);
		
	}

	@Override
	public void deleteAssetIncome(Long incomeId, String operator) {
		ValidateUtils.notNull(incomeId);
		AssetIncome record = new AssetIncome();
		record.setId(incomeId);
		DomainUtils.beforeDelete(record, operator);
		assetIncomeMapper.updateByPrimaryKeySelective(record);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void signAllAssetByCounty(AssetSignDto signDto) {
		Long incomeId = signDto.getIncomeId();
		if (incomeId== null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"入库失败，待签收资产没有对应的入库单，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		AssetIncome ai = getIncomeById(incomeId);
		if (!ai.getReceiverWorkno().equals(signDto.getOperator())) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"入库失败，该资产不属于您，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		List<AssetRolloutIncomeDetail> dList = assetRolloutIncomeDetailBO.queryListByIncomeId(incomeId);
		if (CollectionUtils.isNotEmpty(dList)) {
			//签收资产
			assetRolloutIncomeDetailBO.signAssetByIncomeId(incomeId, signDto.getOperator());
			//更新出入库单状态
			updateStatus(incomeId, AssetIncomeStatusEnum.DONE, signDto.getOperator());
			Long rolloutId = dList.get(0).getRolloutId();
			if (rolloutId != null) {
				assetRolloutBO.updateStatus(rolloutId, AssetRolloutStatusEnum.ROLLOUT_DONE, signDto.getOperator());
			}
		}
	}
	

	
}
