package com.taobao.cun.auge.asset.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetCheckInfoBO;
import com.taobao.cun.auge.asset.bo.AssetCheckTaskBO;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("assetCheckService")
@HSFProvider(serviceInterface = AssetCheckService.class)
public class AssetCheckServiceImpl implements AssetCheckService {

	private static final Logger logger = LoggerFactory.getLogger(AssetCheckService.class);

	@Autowired
	private AssetCheckInfoBO assetCheckInfoBO;

	@Autowired
	private AssetCheckTaskBO assetCheckTaskBO;

	@Autowired
	private AssetBO assetBO;

	@Override
	public PageDto<AssetCheckTaskDto> listTasks(AssetCheckTaskCondition param) {
		return assetCheckTaskBO.listTasks(param);
	}

	@Override
	public PageDto<AssetCheckInfoDto> listInfoForOrg(AssetCheckInfoCondition param) {
		return assetCheckInfoBO.listInfoForOrg(param);
	}

	@Override
	public void confirmSelect(List<Long> infoIds, Long countyOrgId, String categoryType, OperatorDto ope) {
		assetCheckInfoBO.confirmSelect(infoIds, countyOrgId, categoryType, ope);
	}

	@Override
	public void confirmOne(Long infoId, String aliNo, OperatorDto ope) {
		assetCheckInfoBO.confrimCheckInfo(infoId, aliNo, ope);

	}

	@Override
	public PageDto<AssetDetailDto> listAssetToChecking(Long countyOrgId, Integer pageNum, Integer pageSize) {
		return assetBO.listAssetToChecking(countyOrgId, pageNum, pageSize);
	}

	@Override
	public PageDto<AssetDetailDto> listAssetToCheckingForStation(Long stationId, Integer pageNum, Integer pageSize) {
		return assetBO.listAssetToCheckingForStation(stationId, pageNum, pageSize);
	}

	@Override
	public PageDto<AssetDetailDto> listAssetForStation(Long stationId, Integer pageNum, Integer pageSize) {
		return null;
	}

	@Override
	public Integer getWaitCheckAsset(String categoryType, Long countyOrgId) {
		List<Asset> assets = assetBO.getWaitCheckAsset(categoryType, countyOrgId);
		if (CollectionUtils.isNotEmpty(assets)) {
			return assets.size();
		}
		return 0;
	}

	@Override
	public void initTaskForCounty(List<Long> orgIds) {
		assetCheckTaskBO.initTaskForCounty(orgIds);
	}

	@Override
	public void backOne(Long infoId, String reason, OperatorDto ope) {
		assetCheckInfoBO.backOne(infoId, reason, ope);

	}

	@Override
	public void initTaskForStation(String taskType, String taskCode, Long taobaoUserId) {
		assetCheckTaskBO.initTaskForStation(taskType, taskCode, taobaoUserId);
	}
}
