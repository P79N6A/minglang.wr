package com.taobao.cun.auge.asset.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.asset.bo.AssetCheckInfoBO;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoAddDto;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.CountyCheckCountDto;
import com.taobao.cun.auge.asset.dto.CountyFollowCheckCountDto;
import com.taobao.cun.auge.asset.enums.AssetCheckInfoCategoryTypeEnum;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.AssetCheckInfo;
import com.taobao.cun.auge.dal.mapper.AssetCheckInfoMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

@Component
public class AssetCheckInfoBOImpl implements AssetCheckInfoBO {
	
	@Autowired
	private AssetCheckInfoMapper assetCheckInfoMapper;
	
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
		record.setAssetType(assetType);
		record.setCategory(addDto.getCategoryType());
		record.setAttFile(JSONObject.toJSONString(addDto.getImages()));
		record.setCheckType(addDto.getCheckType());
		record.setSerialNo(addDto.getSerialNo());
		
		if(OperatorTypeEnum.BUC.getCode().equals(addDto.getOperatorType())){
			record.setCheckerAreaId(checkerAreaId);
			record.setCheckerAreaName(checkerAreaName);
			record.setCheckerAreaType(checkerAreaType);
			record.setCheckerId(checkerId);
			record.setCheckerName(checkerName);
			record.setCheckTime(checkTime);
			record.setCountyOrgId(countyOrgId);
			
		}
		DomainUtils.beforeInsert(record, addDto.getOperator());
		assetCheckInfoMapper.insert(record);
		return null;

	}
	
	private String bulidAssetType(String category){
		List<String> itList = new ArrayList<String>();
		itList.add(AssetCheckInfoCategoryTypeEnum.)
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
