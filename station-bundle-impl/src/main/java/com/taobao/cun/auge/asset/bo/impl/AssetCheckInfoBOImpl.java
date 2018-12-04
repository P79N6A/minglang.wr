package com.taobao.cun.auge.asset.bo.impl;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.buc.api.exception.BucException;
import com.alibaba.buc.api.model.enhanced.EnhancedUser;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetCheckInfoBO;
import com.taobao.cun.auge.asset.bo.AssetCheckTaskBO;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoAddDto;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.CountyCheckCountDto;
import com.taobao.cun.auge.asset.dto.CountyFollowCheckCountDto;
import com.taobao.cun.auge.asset.enums.AssetCheckInfoCategoryTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckInfoStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckTaskTaskTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetCheckInfo;
import com.taobao.cun.auge.dal.domain.AssetCheckInfoExample;
import com.taobao.cun.auge.dal.domain.AssetCheckInfoExample.Criteria;
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
	
	@Autowired
	private AssetBO assetBO;
	
	@Override
	public Boolean addCheckInfo(AssetCheckInfoAddDto addDto) {
		Objects.requireNonNull(addDto, "参数不能为空");
		Objects.requireNonNull(addDto.getSerialNo(), "序列号不能为空");
		Objects.requireNonNull(addDto.getCheckType(), "盘点类型不能为空");
		if (!OperatorTypeEnum.BUC.getCode().equals(addDto.getOperatorType()) && 
				!OperatorTypeEnum.HAVANA.getCode().equals(addDto.getOperatorType())){
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"盘点人必须是村小二,或者县小二");
		}
		validateParam(addDto);
		AssetCheckInfo record = new AssetCheckInfo();
		record.setAliNo(addDto.getAliNo());
		record.setAssetType(AssetCheckInfoCategoryTypeEnum.getAssetType(addDto.getCategoryType()));
		record.setCategory(addDto.getCategoryType());
		record.setAttFile(JSONObject.toJSONString(addDto.getImages()));
		record.setCheckType(addDto.getCheckType());
		record.setSerialNo(addDto.getSerialNo());
		record.setCheckTime(new Date());
		record.setStatus(AssetCheckInfoStatusEnum.CHECKED.getCode());
		if(OperatorTypeEnum.BUC.getCode().equals(addDto.getOperatorType())){//县盘点
			record.setCheckerAreaId(addDto.getOperatorOrgId());
			record.setCheckerAreaName(assetCheckTaskBO.getTaskForCounty(addDto.getOperatorOrgId(), AssetCheckTaskTaskTypeEnum.COUNTY_CHECK.getCode()).getOrgName());
			record.setCheckerAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
			record.setCheckerId(addDto.getOperator());
			record.setCheckerName(getWorkerName(addDto.getOperator()));
			record.setCountyOrgId(addDto.getOperatorOrgId());
		}else  if (OperatorTypeEnum.HAVANA.getCode().equals(addDto.getOperatorType())) {//村盘点
			validateAddStation(addDto);
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
	
	private void validateParam(AssetCheckInfoAddDto addDto) {
		if (addDto.getSerialNo() == null) {
			return;
		}
		//序列号，TD码规则校验
		//序列号存在校验
		AssetCheckInfo ai = getCheckInfoBySerialNo(addDto.getSerialNo());
		if (ai!= null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"当前序列号["+addDto.getSerialNo()+"]已提交盘点信息");
		}
		
		Asset a = assetBO.getAssetBySerialNo(addDto.getSerialNo());
		if (a == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"当前序列号["+addDto.getSerialNo()+"]线上不存在，请操作异常盘点");
		}
		
	}

	private void validateAddStation(AssetCheckInfoAddDto addDto) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCategoryEqualTo(addDto.getCategoryType());
		criteria.andCheckerIdEqualTo(addDto.getOperator());
		criteria.andCheckerAreaTypeEqualTo(AssetUseAreaTypeEnum.STATION.getCode());
		AssetCheckInfo ai = ResultUtils.selectOne(assetCheckInfoMapper.selectByExample(example));
		if (ai != null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"当前类型["+AssetCheckInfoCategoryTypeEnum.valueof(addDto.getCategoryType()).getDesc()
					+"]资产已提交盘点信息");

		}
		
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

	@Override
	public AssetCheckInfo getCheckInfoBySerialNo(String serialNo) {
		AssetCheckInfoExample example = new AssetCheckInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andSerialNoEqualTo(serialNo);
		return  ResultUtils.selectOne(assetCheckInfoMapper.selectByExample(example));
	}

}
