package com.taobao.cun.auge.asset.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.convert.AssetRolloutConverter;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutReceiverAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutTypeEnum;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AssetRollout;
import com.taobao.cun.auge.dal.domain.AssetRolloutExample;
import com.taobao.cun.auge.dal.domain.AssetRolloutExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AssetRolloutMapper;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;

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

	@Override
	public Long transferAssetOtherCounty(AssetTransferDto transferDto) {
		ValidateUtils.notNull(transferDto);
		String operator = transferDto.getOperator();
		Long operatorOrgId = transferDto.getOperatorOrgId();
		String operatorName = emp360Adapter.getName(operator);
		String receiverName =  emp360Adapter.getName(transferDto.getReceiverWorkNo());
		
		AssetRolloutDto roDto = new AssetRolloutDto();
		roDto.setApplierWorkno(operator);
		roDto.setApplierName(operatorName);
		
		CuntaoOrgDto applyOrg = cuntaoOrgServiceClient.getCuntaoOrg(operatorOrgId);
		CuntaoOrgDto receiverOrg = cuntaoOrgServiceClient.getCuntaoOrg(Long.parseLong(transferDto.getReceiverAreaId()));
		roDto.setApplierOrgId(operatorOrgId);
		roDto.setApplierOrgName(applyOrg.getName());
		roDto.setReceiverId(transferDto.getReceiverWorkNo());
		roDto.setReceiverName(receiverName);
		roDto.setReceiverAreaType(AssetRolloutReceiverAreaTypeEnum.COUNTY);
		roDto.setReceiverAreaName(receiverOrg.getName());
		roDto.setReceiverAreaId(Long.parseLong(transferDto.getReceiverAreaId()));
		roDto.setReason(transferDto.getReason());
		String remark = "转移至 "+receiverOrg.getName() +"-" +receiverName;
		roDto.setRemark(remark);
		roDto.setStatus(AssetRolloutStatusEnum.WAIT_AUDIT);
		roDto.setType(AssetRolloutTypeEnum.TRANSFER);
		roDto.setLogisticsCost(transferDto.getPayment());
		roDto.setLogisticsDistance(transferDto.getDistance());
		Long rolloutId = addRollout(roDto);
		
		for (Long assetId : transferDto.getTransferAssetIdList()) {
			AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
			detail.setAssetId(assetId);
			//detail.setCategory(category);
			detail.setRolloutId(rolloutId);
			detail.setStatus(AssetRolloutIncomeDetailStatusEnum.WAIT_AUDIT);
			detail.setType(AssetRolloutIncomeDetailTypeEnum.TRANSFER);
			assetRolloutIncomeDetailBO.addDetail(detail);
		}
		return null;
	}


}
