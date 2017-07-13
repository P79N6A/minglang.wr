package com.taobao.cun.auge.asset.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
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
			if (StringUtils.isEmpty(operator)) {
				throw new IllegalArgumentException("test");
			}
			assetBO.saveCuntaoAsset(cuntaoAssetDto,operator);
	}

	@Override
	public CuntaoAssetDto getCuntaoAssetById(Long assetId) {
			return assetBO.getCuntaoAssetById(assetId);
	}

	@Override
	public PageDto<CuntaoAssetDto> queryByPage(AssetQueryCondition cuntaoAssetQueryCondition) {
			return assetBO.queryByPage(cuntaoAssetQueryCondition);
	}

	@Override
	public void signAsset(Long assetId, String operator) {
			assetBO.signAsset(assetId, operator);
	}

	@Override
	public void checkAsset(Long assetId, String operator, CuntaoAssetEnum checkRole) {
			assetBO.checkAsset(assetId, operator,checkRole);
	}

	@Override
	public void callbackAsset(Long assetId, String operator) {
			assetBO.callbackAsset(assetId, operator);
	}

	@Override
	public void deleteAsset(Long assetId,String operator) {
			assetBO.deleteAsset(assetId, operator);
	}

	@Override
	  public PageDto<String> getBoNoByOrgId(Long orgId,Integer pageNum,Integer pageSize){
			return assetBO.getBoNoByOrgId(orgId, pageNum, pageSize);
	}

	@Override
	  public void checkingAssetBatch(List<Long> assetIds,String operator){
			 assetBO.checkingAssetBatch(assetIds, operator);
	}

	@Override
	public CuntaoAssetDto queryAssetByUserAndCategory(Long userid) {
			return assetBO.queryAssetByUserAndCategory(userid);
	}

	@Override
	public CuntaoAssetDto queryAssetBySerialNo(String serialNo) {
			return assetBO.queryAssetBySerialNo(serialNo);
	}
}
