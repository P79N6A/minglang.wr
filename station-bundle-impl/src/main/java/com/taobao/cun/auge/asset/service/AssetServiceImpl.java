package com.taobao.cun.auge.asset.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.dto.AssetCheckDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetPurchaseDto;
import com.taobao.cun.auge.asset.dto.AssetQueryPageCondition;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.dto.AssetSignEvent;
import com.taobao.cun.auge.asset.dto.AssetSignEvent.Content;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.crius.event.ExtEvent;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("assetService")
@HSFProvider(serviceInterface = AssetService.class)
public class AssetServiceImpl implements AssetService{

	private static final Logger logger = LoggerFactory.getLogger(AssetServiceImpl.class);
	
	@Autowired
	private AssetBO assetBO;

	@Autowired
	private AssetRolloutBO assetRolloutBO;

	@Autowired
	private AssetRolloutIncomeDetailBO assetRolloutIncomeDetailBO;

	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;

	@Autowired
	private Emp360Adapter emp360Adapter;
	
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

	@Override
	public Boolean judgeCanBuyAsset(Long stationId) {
		return assetBO.judgeCanBuyAsset(stationId);
	}

	@Override
	public Boolean buyAsset(CuntaoAssetDto assetDto) {
		return assetBO.buyAsset(assetDto);
	}

	@Override
	public AssetRolloutDto getRolloutById(Long id) {
		try {
			return assetRolloutBO.getRolloutDtoById(id);
		} catch (Exception e) {
			logger.error("getRolloutById error，id："+id,e);
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"getRolloutById error");
		}
	}

	@Override
	@Transactional
	public void processAuditAssetTransfer(Long rolloutId, ProcessApproveResultEnum resultEnum) {
		List<Long> assetIdList = assetRolloutIncomeDetailBO.queryListByRolloutId(rolloutId).stream().map
			(AssetRolloutIncomeDetail::getAssetId)
			.collect(Collectors.toList());
		AssetTransferDto transferDto = new AssetTransferDto();
		AssetRolloutDto rolloutDto = assetRolloutBO.getRolloutDtoById(rolloutId);
		transferDto.setTransferAssetIdList(assetIdList);
		transferDto.setOperator(rolloutDto.getApplierWorkno());
		transferDto.setOperatorOrgId(rolloutDto.getApplierOrgId());
		transferDto.setOperatorType(OperatorTypeEnum.BUC);
		transferDto.setReceiverAreaId(rolloutDto.getReceiverAreaId());
		transferDto.setReceiverWorkNo(rolloutDto.getReceiverId());
		if (ProcessApproveResultEnum.APPROVE_REFUSE.equals(resultEnum)) {
			assetBO.disagreeTransferAsset(transferDto);
		} else if (ProcessApproveResultEnum.APPROVE_PASS.equals(resultEnum)) {
			assetBO.agreeTransferAsset(transferDto);
			sendTransferMessage(rolloutId, transferDto);
		}
	}

	private void sendTransferMessage(Long rolloutId, AssetTransferDto transferDto) {
		AssetSignEvent signEvent = new AssetSignEvent();
		signEvent.setAppId("cuntaoCRM");
		signEvent.setReceivers(Collections.singletonList(Long.valueOf(transferDto.getReceiverWorkNo())));
		signEvent.setReceiverType("EMPIDS");
		signEvent.setMsgType("ASSET");
		signEvent.setMsgTypeDetail("SIGN");
		signEvent.setAction("all");
		Content content = signEvent.new Content();
		content.setBizId(rolloutId);
		content.setPublishTime(new Date());
		content.setTitle("资产正在转移中，请您注意签收！");
		content.setContent("自" + cuntaoOrgServiceClient.getCuntaoOrg(transferDto.getOperatorOrgId()) + emp360Adapter.getName(transferDto.getOperator()) + "(" +transferDto.getOperator() + ")申请转移的资产已发出，请您注意签收，查看详情");
		content.setRouteUrl("url");
		signEvent.setContent(content);
		EventDispatcherUtil.dispatch("CRM_ASSET_TRANSFER", new ExtEvent(JSON.toJSONString(signEvent)));
	}

	@Override
	public PageDto<Long> getCheckedAssetId(Integer pageNum, Integer pageSize) {
        Page<Asset> aList = assetBO.getCheckedAsset(pageNum, pageSize);
		List<Long> assetIdList =aList.stream().map(Asset::getId).collect(Collectors.toList());
		return PageDtoUtil.success(aList, assetIdList);
	}

	@Override
	public Boolean checkingAsset(Long assetId, String operator) {
		return assetBO.checkingAsset(assetId, operator);
		
	}

	@Override
	public Long purchase(AssetPurchaseDto assetPurchaseDto) {
		return assetBO.purchase(assetPurchaseDto);
		
	}

	@Override
	public PageDto<AssetDetailDto> queryByPage(AssetQueryPageCondition query) {
		return assetBO.queryByPage(query);
	}

	@Override
	public void delete(Long assetId, String operator) {
		assetBO.delete(assetId, operator);
		
	}

	@Override
	public AssetDetailDto getDetail(Long assetId) {
		return assetBO.getDetail(assetId);
	}

	@Override
	public PageDto<AssetRolloutIncomeDetailDto> queryAssetRiDetailByPage(
			Long assetId, int pageNum, int pageSize) {
		return assetRolloutIncomeDetailBO.queryAssetRiDetailByPage(assetId, pageNum, pageSize);
	}

	@Override
	public List<AssetDetailDto> getDistributeAssetListByStation(Long stationId,
			Long taobaoUserId) {
		return assetBO.getDistributeAssetListByStation(stationId, taobaoUserId);
	}

	@Override
	public List<AssetDetailDto> getUseAssetListByStation(Long stationId,
			Long taobaoUserId) {
		return assetBO.getUseAssetListByStation(stationId, taobaoUserId);
	}

	@Override
	public Boolean checkAsset(AssetCheckDto checkDto) {
		return assetBO.checkAsset(checkDto);
	}

	@Override
	public Boolean signAssetByStation(AssetDto signDto) {
		return assetBO.signAssetByStation(signDto);
	}
}
