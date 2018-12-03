package com.taobao.cun.auge.asset.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.asset.bo.AssetCheckInfoBO;
import com.taobao.cun.auge.asset.bo.AssetCheckTaskBO;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoAddDto;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.CountyCheckCountDto;
import com.taobao.cun.auge.asset.dto.CountyFollowCheckCountDto;
import com.taobao.cun.auge.asset.dto.FinishTaskForCountyDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("assetCheckMobileService")
@HSFProvider(serviceInterface = AssetCheckMobileService.class)
public class AssetCheckMobileServiceImpl implements AssetCheckMobileService {
	
	private static final Logger logger = LoggerFactory.getLogger(AssetCheckMobileService.class);
	@Autowired
	private AssetCheckInfoBO assetCheckInfoBO;
	
	@Autowired
	private AssetCheckTaskBO assetCheckTaskBO;
	
	@Override
	public Result<Boolean> finishTaskForStation(Long taobaoUserId) {
		try {
			return Result.of(assetCheckTaskBO.finishTaskForStation(taobaoUserId));
		} catch (AugeBusinessException e1) {
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, null, e1.getMessage());
			return Result.of(errorInfo);
		}catch (Exception e) {
			logger.error("AssetCheckMobileService.finishTaskForCounty error! param:"+taobaoUserId, e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<Boolean> finishTaskForCounty(FinishTaskForCountyDto param) {
		try {
			return Result.of(assetCheckTaskBO.finishTaskForCounty(param));
		} catch (AugeBusinessException e1) {
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, null, e1.getMessage());
			return Result.of(errorInfo);
		}catch (Exception e) {
			logger.error("AssetCheckMobileService.finishTaskForCounty error! param:"+ JSON.toJSONString(param), e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<Boolean> addCheckInfo(AssetCheckInfoAddDto addDto) {
		try {
			return Result.of(assetCheckInfoBO.addCheckInfo(addDto));
		} catch (AugeBusinessException e1) {
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, null, e1.getMessage());
			return Result.of(errorInfo);
		}catch (Exception e) {
			logger.error("AssetCheckMobileService.addCheckInfo error! param:"+ JSON.toJSONString(addDto), e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<Boolean> delCheckInfo(Long infoId, OperatorDto operator) {
		try {
			return Result.of(assetCheckInfoBO.delCheckInfo(infoId,operator));
		} catch (AugeBusinessException e1) {
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, null, e1.getMessage());
			return Result.of(errorInfo);
		}catch (Exception e) {
			logger.error("AssetCheckMobileService.delCheckInfo error! param:"+infoId, e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<PageDto<AssetCheckInfoDto>> listInfo(AssetCheckInfoCondition param) {
		try {
			return Result.of(assetCheckInfoBO.listInfo(param));
		} catch (AugeBusinessException e1) {
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, null, e1.getMessage());
			return Result.of(errorInfo);
		}catch (Exception e) {
			logger.error("AssetCheckMobileService.listInfo error! param:"+ JSON.toJSONString(param), e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<CountyCheckCountDto> getCountyCheckCount(Long countyOrgId) {
		try {
			return Result.of(assetCheckInfoBO.getCountyCheckCount(countyOrgId));
		} catch (AugeBusinessException e1) {
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, null, e1.getMessage());
			return Result.of(errorInfo);
		}catch (Exception e) {
			logger.error("AssetCheckMobileService.getCountyCheckCount error! param:"+ countyOrgId, e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<CountyFollowCheckCountDto> getCountyFollowCheckCount(Long countyOrgId) {
		try {
			return Result.of(assetCheckInfoBO.getCountyFollowCheckCount(countyOrgId));
		} catch (AugeBusinessException e1) {
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, null, e1.getMessage());
			return Result.of(errorInfo);
		}catch (Exception e) {
			logger.error("AssetCheckMobileService.getCountyFollowCheckCount error! param:"+ countyOrgId, e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}
}
