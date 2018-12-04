package com.taobao.cun.auge.asset.bo.impl;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.buc.api.exception.BucException;
import com.alibaba.buc.api.model.enhanced.EnhancedUser;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.asset.bo.AssetCheckInfoBO;
import com.taobao.cun.auge.asset.bo.AssetCheckTaskBO;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoAddDto;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.CountyCheckCountDto;
import com.taobao.cun.auge.asset.dto.CountyFollowCheckCountDto;
import com.taobao.cun.auge.asset.enums.AssetCheckInfoCategoryTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckTaskTaskTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.AssetCheckInfo;
import com.taobao.cun.auge.dal.domain.AssetCheckTask;
import com.taobao.cun.auge.dal.mapper.AssetCheckInfoMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

@Component
public class AssetCheckInfoBOImpl implements AssetCheckInfoBO {
	
	@Autowired
	private EnhancedUserQueryService enhancedUserQueryService;
	
	@Autowired
	private AssetCheckInfoMapper assetCheckInfoMapper;
	
	@Autowired
	private AssetCheckTaskBO assetCheckTaskBO;
	
	@Override
	public Boolean addCheckInfo(AssetCheckInfoAddDto addDto) {
		Objects.requireNonNull(addDto, "参数不能为空");
		Objects.requireNonNull(addDto.getSerialNo(), "序列号不能为空");
		Objects.requireNonNull(addDto.getCheckType(), "盘点类型不能为空");
		if (!OperatorTypeEnum.BUC.getCode().equals(addDto.getOperatorType()) && 
				!OperatorTypeEnum.HAVANA.getCode().equals(addDto.getOperatorType())){
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"盘点人必须是村小二,或者县小二");
		}
		AssetCheckInfo record = new AssetCheckInfo();
		record.setAliNo(addDto.getAliNo());
		record.setAssetType(AssetCheckInfoCategoryTypeEnum.getAssetType(addDto.getCategoryType()));
		record.setCategory(addDto.getCategoryType());
		record.setAttFile(JSONObject.toJSONString(addDto.getImages()));
		record.setCheckType(addDto.getCheckType());
		record.setSerialNo(addDto.getSerialNo());
		record.setCheckTime(new Date());
		if(OperatorTypeEnum.BUC.getCode().equals(addDto.getOperatorType())){//县盘点
			record.setCheckerAreaId(addDto.getOperatorOrgId());
			record.setCheckerAreaName(assetCheckTaskBO.getTaskForCounty(addDto.getOperatorOrgId(), AssetCheckTaskTaskTypeEnum.COUNTY_CHECK.getCode()).getOrgName());
			record.setCheckerAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
			record.setCheckerId(addDto.getOperator());
			record.setCheckerName(getWorkerName(addDto.getOperator()));
			record.setCountyOrgId(addDto.getOperatorOrgId());
		}else  if (OperatorTypeEnum.BUC.getCode().equals(addDto.getOperatorType())) {//村盘点
			AssetCheckTask  at = assetCheckTaskBO.getTaskForStation(addDto.getOperator());
			record.setCheckerAreaId(at.getStationId());
			record.setCheckerAreaName(at.getStationName());
			record.setCheckerAreaType(AssetUseAreaTypeEnum.STATION.getCode());
			record.setCheckerId(addDto.getOperator());
			record.setCheckerName(at.getCheckerName());
			record.setCountyOrgId(at.getOrgId());
		}
		DomainUtils.beforeInsert(record, addDto.getOperator());
		assetCheckInfoMapper.insert(record);
		return null;

	}
	
	private String getWorkerName(String workNo){
		try {
			EnhancedUser enhancedUser = enhancedUserQueryService.getUser(workNo);
			return enhancedUser.getLastName();
		} catch (BucException e) {
		}
		return null;
	}
	

	@Override
	public Boolean delCheckInfo(Long infoId, OperatorDto operator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean confrimCheckInfo(Long infoId, OperatorDto operator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDto<AssetCheckInfoDto> listInfoForOrg(AssetCheckInfoCondition param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDto<AssetCheckInfoDto> listInfo(AssetCheckInfoCondition param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssetCheckInfoDto getCheckInfoById(Long infoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CountyCheckCountDto getCountyCheckCount(Long countyOrgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CountyFollowCheckCountDto getCountyFollowCheckCount(Long countyOrgId) {
		// TODO Auto-generated method stub
		return null;
	}

}
