package com.taobao.cun.auge.asset.service;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("assetService")
@HSFProvider(serviceInterface = AssetService.class)

public class AssetServiceImpl implements AssetService{

	private static final Logger logger = LoggerFactory.getLogger(AssetServiceImpl.class);
	
	@Autowired
	private AssetBO assetBO;
	
	@Override
	public void saveAsset(CuntaoAssetDto cuntaoAssetDto,String operator) {
		try {
			assetBO.saveCuntaoAsset(cuntaoAssetDto,operator);
		} catch (Exception e) {
			logger.error("saveAsset error",e);
			throw new AugeBusinessException("saveAsset error");
		}
	
	}

	@Override
	public CuntaoAssetDto getCuntaoAssetById(Long assetId) {
		try {
			return assetBO.getCuntaoAssetById(assetId);
		} catch (Exception e) {
			logger.error("getCuntaoAssetById error,assetId["+assetId+"]",e);
			throw new AugeBusinessException("getCuntaoAssetById error");
		}
		
	}

	@Override
	public PageDto<CuntaoAssetDto> queryByPage(AssetQueryCondition cuntaoAssetQueryCondition) {
		try {
			return assetBO.queryByPage(cuntaoAssetQueryCondition);
		} catch (Exception e) {
			logger.error("queryByPage error",e);
			throw new AugeBusinessException("queryByPage error");
		}
		
	}

	@Override
	public void signAsset(Long assetId, String operator) {
		try {
			assetBO.signAsset(assetId, operator);
		} catch (Exception e) {
			logger.error("signAsset error assetId："+assetId,e);
			throw new AugeBusinessException("signAsset error");
		}
		
	}

	@Override
	public void checkAsset(Long assetId, String operator, CuntaoAssetEnum checkRole) {
		try {
			assetBO.checkAsset(assetId, operator,checkRole);
		} catch (Exception e) {
			logger.error("checkAsset error assetId"+assetId,e);
			throw new AugeBusinessException("checkAsset error");
		}
		
	}

	@Override
	public void callbackAsset(Long assetId, String operator) {
		try {
			assetBO.callbackAsset(assetId, operator);
		} catch (Exception e) {
			logger.error("callbackAsset error assetId："+assetId,e);
			throw new AugeBusinessException("callbackAsset error");
		}
		
	}

	@Override
	public void deleteAsset(Long assetId,String operator) {
		try {
			assetBO.deleteAsset(assetId, operator);
		} catch (Exception e) {
			logger.error("deleteAsset error assetId："+assetId,e);
			throw new AugeBusinessException("deleteAsset error");
		}
		
		
	}

	@Override
	  public PageDto<String> getBoNoByOrgId(Long orgId,Integer pageNum,Integer pageSize){
		try {
			return assetBO.getBoNoByOrgId(orgId, pageNum, pageSize);
		} catch (Exception e) {
			logger.error("getBoNoByOrgId error",e);
			throw new AugeBusinessException("getBoNoByOrgId error");
		}
	}

	@Override
	  public void checkingAssetBatch(List<Long> assetIds,String operator){
		try {
			 assetBO.checkingAssetBatch(assetIds, operator);
		} catch (Exception e) {
			logger.error("checkingAssetBatch error assetIds："+assetIds,e);
			throw new AugeBusinessException("checkingAssetBatch error");
		}
		 
	}

	@Override
	public CuntaoAssetDto queryAssetByUserAndCategory(Long userid) {
		try {
			return assetBO.queryAssetByUserAndCategory(userid);
		} catch (Exception e) {
			logger.error("queryAssetByUserAndCategory error userid："+userid,e);
			throw new AugeBusinessException("queryAssetByUserAndCategory error");
		}
		
	}

	@Override
	public CuntaoAssetDto queryAssetBySerialNo(String serialNo) {
		try {
			return assetBO.queryAssetBySerialNo(serialNo);
		} catch (Exception e) {
			logger.error("queryAssetBySerialNo error，serialNo："+serialNo,e);
			throw new AugeBusinessException("queryAssetBySerialNo error");
		}
		
	}

	@Override
	public List<CategoryAssetListDto> getCategoryAssetListByUserId(String userId) {
		return assetBO.getCategoryAssetListByUserId(userId);
	}

	@Override
	public List<AreaAssetListDto> getAreaAssetListByUserId(String userId) {
		return null;
	}

	@Override
	public CategoryAssetDetailDto getCategoryAssetDetail(AssetDetailQueryCondition condition) {
		return null;
	}

	@Override
	public AreaAssetDetailDto getAreaAssetDetail(AssetDetailQueryCondition condition) {
		return null;
	}
}
