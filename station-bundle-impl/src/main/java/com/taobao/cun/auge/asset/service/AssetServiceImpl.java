package com.taobao.cun.auge.asset.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetSignEvent;
import com.taobao.cun.auge.asset.dto.AssetSignEvent.Content;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.event.EventDispatcherUtil;
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
	private AssetRolloutIncomeDetailBO detailBO;

	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;

	@Autowired
	private Emp360Adapter emp360Adapter;
	
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
	public AssetRolloutDto getRolloutById(Long id) {
		try {
			return assetRolloutBO.getRolloutDtoById(id);
		} catch (Exception e) {
			logger.error("getRolloutById error，id："+id,e);
			throw new AugeBusinessException("getRolloutById error");
		}
	}

	@Override
	@Transactional
	public void processAuditAssetTransfer(Long rolloutId, ProcessApproveResultEnum resultEnum) {
		List<Long> assetIdList = detailBO.queryListByRolloutId(rolloutId).stream().map
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

}
