package com.taobao.cun.auge.asset.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.convert.AssetRolloutConverter;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutCancelDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.enums.AssetIncomeApplierAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutReceiverAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetRollout;
import com.taobao.cun.auge.dal.domain.AssetRolloutExample;
import com.taobao.cun.auge.dal.domain.AssetRolloutExample.Criteria;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.dal.mapper.AssetRolloutMapper;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

@Component
public class AssetRolloutBOImpl implements AssetRolloutBO {
	
	@Autowired
	private AssetRolloutMapper assetRolloutMapper;
	
	@Autowired
	private Emp360Adapter emp360Adapter;
	
	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
	@Autowired
	private AssetRolloutIncomeDetailBO assetRolloutIncomeDetailBO;
	
	@Autowired
	private AssetIncomeBO assetIncomeBO;
	
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
	public List<Long> cancelRolleout(AssetRolloutCancelDto cancelDto) {
		ValidateUtils.notNull(cancelDto);
		Long rolloutId = cancelDto.getRolloutId();
		ValidateUtils.notNull(cancelDto.getRolloutId());
		AssetRollout ar = getRolloutById(rolloutId);
		if (!canCancelStatus(ar.getStatus())) {
			throw new AugeBusinessException("当前出库状态["+AssetRolloutStatusEnum.valueof(ar.getStatus()).getDesc()+"]不能撤销");
		}
		List<AssetRolloutIncomeDetail> dList = assetRolloutIncomeDetailBO.queryListByRolloutId(rolloutId);
		if (!dList.stream().allMatch(asset -> AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode().equals(asset.getStatus()))) {
			throw new AugeBusinessException("部分资产已经签收，不能撤销");
		}
		List<Long> assetIdList =dList.stream().map(AssetRolloutIncomeDetail::getAssetId).collect(Collectors.toList());
	    
		return assetIdList;
		
		
	}
	private Boolean canCancelStatus(String  status) {
		List<String> l = new  ArrayList<String>();
		l.add(AssetRolloutStatusEnum.WAIT_AUDIT.getCode());
		l.add(AssetRolloutStatusEnum.WAIT_ROLLOUT.getCode());
		l.add(AssetRolloutStatusEnum.WAIT_COMPENSATE.getCode());
		if (l.contains(status)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
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

	@Override
	public Long transferAssetOtherCounty(AssetTransferDto transferDto,List<Asset> assetList) {
		ValidateUtils.notNull(transferDto);
		String operator = transferDto.getOperator();
		Long operatorOrgId = transferDto.getOperatorOrgId();
		String operatorName = emp360Adapter.getName(operator);
		String receiverName =  emp360Adapter.getName(transferDto.getReceiverWorkNo());
		CuntaoOrgDto applyOrg = cuntaoOrgServiceClient.getCuntaoOrg(operatorOrgId);
		CuntaoOrgDto receiverOrg = cuntaoOrgServiceClient.getCuntaoOrg(Long.parseLong(transferDto.getReceiverAreaId()));
		//创建出库单
		AssetRolloutDto roDto = new AssetRolloutDto();
		roDto.setApplierWorkno(operator);
		roDto.setApplierName(operatorName);
		roDto.setStatus(AssetRolloutStatusEnum.WAIT_AUDIT);
		roDto.setApplierOrgId(operatorOrgId);
		roDto.setApplierOrgName(applyOrg.getName());
		roDto.setReceiverId(transferDto.getReceiverWorkNo());
		roDto.setReceiverName(receiverName);
		roDto.setReceiverAreaType(AssetRolloutReceiverAreaTypeEnum.COUNTY);
		roDto.setReceiverAreaName(receiverOrg.getName());
		roDto.setReceiverAreaId(Long.parseLong(transferDto.getReceiverAreaId()));
		roDto.setReason(transferDto.getReason());
		roDto.setRemark("转移至 "+receiverOrg.getName() +"-" +receiverName);
		roDto.setType(AssetRolloutTypeEnum.TRANSFER);
		roDto.setLogisticsCost(transferDto.getPayment());
		roDto.setLogisticsDistance(transferDto.getDistance());
		Long rolloutId = addRollout(roDto);
		
		// 出入库单详情  入库单id 在审批通过时 创建 并更新上去
		for (Asset asset: assetList) {
			AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
			detail.setAssetId(asset.getId());
			detail.setCategory(asset.getCategory());
			detail.setRolloutId(rolloutId);
			detail.setStatus(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN);
			detail.setType(AssetRolloutIncomeDetailTypeEnum.TRANSFER);
			assetRolloutIncomeDetailBO.addDetail(detail);
		}
		return rolloutId;
	}
	
	@Override
	public Long transferAssetSelfCounty(AssetTransferDto transferDto, List<Asset> assetList) {
		ValidateUtils.notNull(transferDto);
		String operator = transferDto.getOperator();
		Long operatorOrgId = transferDto.getOperatorOrgId();
		String operatorName = emp360Adapter.getName(operator);
		String receiverName =  emp360Adapter.getName(transferDto.getReceiverWorkNo());
		CuntaoOrgDto applyOrg = cuntaoOrgServiceClient.getCuntaoOrg(operatorOrgId);
		CuntaoOrgDto receiverOrg = cuntaoOrgServiceClient.getCuntaoOrg(Long.parseLong(transferDto.getReceiverAreaId()));
		//创建出库单
		AssetRolloutDto roDto = new AssetRolloutDto();
		roDto.setApplierWorkno(operator);
		roDto.setApplierName(operatorName);
		roDto.setStatus(AssetRolloutStatusEnum.WAIT_ROLLOUT);
		roDto.setApplierOrgId(operatorOrgId);
		roDto.setApplierOrgName(applyOrg.getName());
		roDto.setReceiverId(transferDto.getReceiverWorkNo());
		roDto.setReceiverName(receiverName);
		roDto.setReceiverAreaType(AssetRolloutReceiverAreaTypeEnum.COUNTY);
		roDto.setReceiverAreaName(receiverOrg.getName());
		roDto.setReceiverAreaId(Long.parseLong(transferDto.getReceiverAreaId()));
		roDto.setReason(transferDto.getReason());
		roDto.setRemark("转移至 "+receiverOrg.getName() +"-" +receiverName);
		roDto.setType(AssetRolloutTypeEnum.TRANSFER);
		roDto.copyOperatorDto(transferDto);
		Long rolloutId = addRollout(roDto);
		
		//创建入库单
		AssetIncomeDto icDto = new AssetIncomeDto();
		icDto.setApplierAreaId(operatorOrgId);
		icDto.setApplierAreaName(applyOrg.getName());
		icDto.setApplierAreaType(AssetIncomeApplierAreaTypeEnum.COUNTY);
		icDto.setApplierId(operator);
		icDto.setApplierName(operatorName);
		icDto.setReceiverName(receiverName);
		icDto.setReceiverOrgId(Long.parseLong(transferDto.getReceiverAreaId()));
		icDto.setReceiverOrgName(receiverOrg.getName());
		icDto.setReceiverWorkno(transferDto.getReceiverWorkNo());
		icDto.setRemark(applyOrg.getName()+"-"+operatorName+" 申请转移");
		icDto.setStatus(AssetIncomeStatusEnum.TODO);
		icDto.setType(AssetIncomeTypeEnum.TRANSFER);
		Long incomeId = assetIncomeBO.addIncome(icDto);
		
		// 出入库单详情
		for (Asset asset: assetList) {
			AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
			detail.setAssetId(asset.getId());
			detail.setCategory(asset.getCategory());
			detail.setRolloutId(rolloutId);
			detail.setIncomeId(incomeId);
			detail.setStatus(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN);
			detail.setType(AssetRolloutIncomeDetailTypeEnum.TRANSFER);
			assetRolloutIncomeDetailBO.addDetail(detail);
		}
		return rolloutId;
	}


}
