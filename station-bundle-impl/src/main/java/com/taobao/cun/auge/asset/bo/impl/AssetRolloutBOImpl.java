package com.taobao.cun.auge.asset.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.convert.AssetRolloutConverter;
import com.taobao.cun.auge.asset.dto.AssetAppMessageDto;
import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDistributeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutCancelDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetScrapDto;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.enums.AssetIncomeApplierAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeSignTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutReceiverAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutTypeEnum;
import com.taobao.cun.auge.asset.service.AssetFlowService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetRollout;
import com.taobao.cun.auge.dal.domain.AssetRolloutExample;
import com.taobao.cun.auge.dal.domain.AssetRolloutExample.Criteria;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.AssetRolloutMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class AssetRolloutBOImpl implements AssetRolloutBO {
	
	private static final Logger logger = LoggerFactory.getLogger(AssetRolloutBO.class);
	
	@Autowired
	private AssetRolloutMapper assetRolloutMapper;
	
	@Autowired
	private Emp360Adapter emp360Adapter;
	
	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
	@Autowired
	private AssetRolloutIncomeDetailBO assetRolloutIncomeDetailBO;
	
	@Autowired
	private AssetIncomeBO assetIncomeBO;
	
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;

	@Autowired
	private StationBO stationBO;

	@Autowired
	private AssetBO assetBO;
	
	@Autowired
	private DiamondConfiguredProperties configuredProperties;
	
    @Autowired
    private AssetFlowService assetFlowService;

    @Autowired
	private UicReadAdapter uicReadAdapter;
	
	@Override
	public PageDto<AssetRolloutDto> getRolloutList(AssetRolloutQueryCondition condition) {
		ValidateUtils.notNull(condition);
		ValidateUtils.notNull(condition.getWorkNo());
		AssetRolloutExample example = new AssetRolloutExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n").andApplierWorknoEqualTo(condition.getWorkNo());
		if (condition.getOperatorOrgId() != null) {
			criteria.andApplierOrgIdEqualTo(condition.getOperatorOrgId());
		}
		example.setOrderByClause("GMT_CREATE DESC");
		PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
		Page<AssetRollout>   roList = (Page<AssetRollout>)assetRolloutMapper.selectByExample(example);
		List<AssetRolloutDto> dtoList = new ArrayList<AssetRolloutDto>();
		
        for (AssetRollout ai : roList) {
        	AssetRolloutDto dto = AssetRolloutConverter.toAssetRolloutDto(ai);
        	bulidCount(dto);
            dtoList.add(dto);
        }
        return PageDtoUtil.success(roList, dtoList);
	}
	
	private void bulidCount(AssetRolloutDto dto){
		List<AssetCategoryCountDto> res = assetRolloutIncomeDetailBO.queryCountByRolloutId(dto.getId(), null);
		res.forEach(n -> n.setCategoryName(configuredProperties.getCategoryMap().get(n.getCategory())));
		dto.setCountList(res);
		List<AssetCategoryCountDto> res1 = assetRolloutIncomeDetailBO.queryCountByRolloutId(dto.getId(),AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN);
		res1.forEach(n -> n.setCategoryName(configuredProperties.getCategoryMap().get(n.getCategory())));
		dto.setWaitSignCountList(res1);
	}

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long addRollout(AssetRolloutDto param) {
		ValidateUtils.notNull(param);
		AssetRollout record = AssetRolloutConverter.toAssetRollout(param);
		DomainUtils.beforeInsert(record, param.getOperator());
		assetRolloutMapper.insert(record);
		return record.getId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void cancelRolleoutAsset(AssetRolloutCancelDto cancelDto) {
		ValidateUtils.notNull(cancelDto);
		Long assetId = cancelDto.getAssetId();
		//撤销详情
		AssetRolloutIncomeDetail detail = assetRolloutIncomeDetailBO.cancel(assetId, cancelDto.getOperator());
	    //撤销出库单
		List<AssetRolloutIncomeDetail> detailList = assetRolloutIncomeDetailBO.queryListByRolloutId(detail.getRolloutId());
		if (detailList.stream().allMatch(asset -> AssetRolloutIncomeDetailStatusEnum.CANCEL.getCode().equals(asset.getStatus()))) {
			AssetRollout record = new AssetRollout();
			record.setId(detail.getRolloutId());
			record.setStatus(AssetRolloutStatusEnum.CANCEL.getCode());
			DomainUtils.beforeUpdate(record, cancelDto.getOperator());
			assetRolloutMapper.updateByPrimaryKeySelective(record);
			if (detail.getIncomeId() != null) {
				assetIncomeBO.cancelAssetIncome(detail.getIncomeId(), cancelDto.getOperator());
			}
			
			//取消流程
			AssetRollout ar = getRolloutById(detail.getRolloutId());
			if (AssetRolloutTypeEnum.TRANSFER.getCode().equals(ar.getType())) {
				if (!Objects.equals(ar.getApplierOrgId(), ar.getReceiverAreaId())) {
					assetFlowService.cancelTransferFlow(detail.getRolloutId(), cancelDto.getOperator());
				}
			}
		}else if (detailList.stream().noneMatch(asset -> AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode().equals(asset.getStatus()))) {
			AssetRollout record = new AssetRollout();
			record.setId(detail.getRolloutId());
			record.setStatus(AssetRolloutStatusEnum.ROLLOUT_DONE.getCode());
			DomainUtils.beforeUpdate(record, cancelDto.getOperator());
			assetRolloutMapper.updateByPrimaryKeySelective(record);
			if (detail.getIncomeId() != null) {
				assetIncomeBO.updateStatus(detail.getIncomeId(), AssetIncomeStatusEnum.DONE, cancelDto.getOperator());
			}
		}
	}

	@Override
	public AssetRollout getRolloutById(Long rolloutId) {
		ValidateUtils.notNull(rolloutId);
		return assetRolloutMapper.selectByPrimaryKey(rolloutId);
	}

	@Override
	public AssetRolloutDto getRolloutDtoById(Long rolloutId) {
		AssetRolloutDto dto = AssetRolloutConverter.toAssetRolloutDto(getRolloutById(rolloutId));
		bulidCount(dto);
		return dto;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
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
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long transferAssetOtherCounty(AssetTransferDto transferDto,List<Asset> assetList) {
		ValidateUtils.notNull(transferDto);
		String operator = transferDto.getOperator();
		Long operatorOrgId = transferDto.getOperatorOrgId();
		String operatorName = getWorkName(operator);
		String receiverName =  getWorkName(transferDto.getReceiverWorkNo());
		
		String applyOrgName = getOrgName(operatorOrgId);
		String receiverOrgName = getOrgName(transferDto.getReceiverAreaId());
		//创建出库单
		AssetRolloutDto roDto = new AssetRolloutDto();
		roDto.setApplierWorkno(operator);
		roDto.setApplierName(operatorName);
		roDto.setStatus(AssetRolloutStatusEnum.WAIT_AUDIT);
		roDto.setApplierOrgId(operatorOrgId);
		roDto.setApplierOrgName(applyOrgName);
		roDto.setReceiverId(transferDto.getReceiverWorkNo());
		roDto.setReceiverName(receiverName);
		roDto.setReceiverAreaType(AssetRolloutReceiverAreaTypeEnum.COUNTY);
		roDto.setReceiverAreaName(receiverOrgName);
		roDto.setReceiverAreaId(transferDto.getReceiverAreaId());
		roDto.setReason(transferDto.getReason());
		roDto.setRemark("转移至 "+receiverOrgName +"-" +receiverName);
		roDto.setType(AssetRolloutTypeEnum.TRANSFER);
		roDto.setLogisticsCost(transferDto.getPayment());
		roDto.setLogisticsDistance(transferDto.getDistance());
		roDto.copyOperatorDto(transferDto);
		Long rolloutId = addRollout(roDto);
		
		// 出入库单详情  入库单id 在审批通过时 创建 并更新上去
		for (Asset asset: assetList) {
			AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
			detail.setAssetId(asset.getId());
			detail.setCategory(asset.getCategory());
			detail.setRolloutId(rolloutId);
			detail.setStatus(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN);
			detail.setType(AssetRolloutIncomeDetailTypeEnum.TRANSFER);
			detail.setOperatorTime(new Date());
			detail.copyOperatorDto(transferDto);
			assetRolloutIncomeDetailBO.addDetail(detail);
		}
		return rolloutId;
	}
	
	private String getOrgName(Long orgId){
		String name = "";
		try {
			CuntaoOrgDto orgDto = cuntaoOrgServiceClient.getCuntaoOrg(orgId);
			if (orgDto !=null) {
				name= orgDto.getName();
			}
			return name;
		} catch (Exception e) {
			logger.error("assetRolloutBO.getOrgName error param:"+orgId,e);
			throw e;
		}
	}
	private String getWorkName(String workNo){
		try {
			return emp360Adapter.getName(workNo);
		} catch (Exception e) {
			logger.error("assetRolloutBO.getWorkName error param:"+workNo,e);
			throw e;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long transferAssetSelfCounty(AssetTransferDto transferDto, List<Asset> assetList,AssetIncomeSignTypeEnum signType) {
		ValidateUtils.notNull(transferDto);
		String operator = transferDto.getOperator();
		Long operatorOrgId = transferDto.getOperatorOrgId();
		String operatorName = getWorkName(operator);
		String receiverName =  getWorkName(transferDto.getReceiverWorkNo());
		String applyOrgName = getOrgName(operatorOrgId);
		String receiverOrgName = getOrgName(transferDto.getReceiverAreaId());
		//创建出库单
		AssetRolloutDto roDto = new AssetRolloutDto();
		roDto.setApplierWorkno(operator);
		roDto.setApplierName(operatorName);
		roDto.setStatus(AssetRolloutStatusEnum.WAIT_ROLLOUT);
		roDto.setApplierOrgId(operatorOrgId);
		roDto.setApplierOrgName(applyOrgName);
		roDto.setReceiverId(transferDto.getReceiverWorkNo());
		roDto.setReceiverName(receiverName);
		roDto.setReceiverAreaType(AssetRolloutReceiverAreaTypeEnum.COUNTY);
		roDto.setReceiverAreaName(receiverOrgName);
		roDto.setReceiverAreaId(transferDto.getReceiverAreaId());
		roDto.setReason(transferDto.getReason());
		roDto.setRemark("转移至 "+receiverOrgName +"-" +receiverName);
		roDto.setType(AssetRolloutTypeEnum.TRANSFER);
		roDto.copyOperatorDto(transferDto);
		Long rolloutId = addRollout(roDto);
		
		//创建入库单
		AssetIncomeDto icDto = new AssetIncomeDto();
		icDto.setApplierAreaId(operatorOrgId);
		icDto.setApplierAreaName(applyOrgName);
		icDto.setApplierAreaType(AssetIncomeApplierAreaTypeEnum.COUNTY);
		icDto.setApplierId(operator);
		icDto.setApplierName(operatorName);
		icDto.setReceiverName(receiverName);
		icDto.setReceiverOrgId(transferDto.getReceiverAreaId());
		icDto.setReceiverOrgName(receiverOrgName);
		icDto.setReceiverWorkno(transferDto.getReceiverWorkNo());
		icDto.setRemark(applyOrgName+"-"+operatorName+" 申请转移");
		icDto.setStatus(AssetIncomeStatusEnum.TODO);
		icDto.setType(AssetIncomeTypeEnum.TRANSFER);
		icDto.setSignType(signType);
		icDto.copyOperatorDto(transferDto);
		Long incomeId = assetIncomeBO.addIncome(icDto);
		
		// 出入库单详情
		for (Asset asset: assetList) {
			AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
			detail.setAssetId(asset.getId());
			detail.setCategory(asset.getCategory());
			detail.setRolloutId(rolloutId);
			detail.setIncomeId(incomeId);
			detail.setStatus(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN);
			detail.setType(AssetRolloutIncomeDetailTypeEnum.TRANSFER);
			detail.setOperatorTime(new Date());
			detail.copyOperatorDto(transferDto);
			assetRolloutIncomeDetailBO.addDetail(detail);
		}
		return rolloutId;
	}
	
	@Override
    public void transferAssetOtherCounty(AssetRolloutDto rolloutDto) {
		//创建入库单
		AssetIncomeDto icDto = new AssetIncomeDto();
		icDto.setApplierAreaId(rolloutDto.getApplierOrgId());
		icDto.setApplierAreaName(rolloutDto.getApplierOrgName());
		icDto.setApplierAreaType(AssetIncomeApplierAreaTypeEnum.COUNTY);
		icDto.setApplierId(rolloutDto.getApplierWorkno());
		icDto.setApplierName(rolloutDto.getApplierName());
		icDto.setReceiverName(rolloutDto.getReceiverName());
		icDto.setReceiverOrgId(rolloutDto.getReceiverAreaId());
		icDto.setReceiverOrgName(rolloutDto.getReceiverAreaName());
		icDto.setReceiverWorkno(rolloutDto.getReceiverId());
		icDto.setRemark(rolloutDto.getApplierOrgName()+"-"+rolloutDto.getApplierName()+" 申请转移");
		icDto.setStatus(AssetIncomeStatusEnum.TODO);
		icDto.setType(AssetIncomeTypeEnum.TRANSFER);
		icDto.setSignType(AssetIncomeSignTypeEnum.SCAN);
		icDto.copyOperatorDto(OperatorDto.defaultOperator());
		Long incomeId = assetIncomeBO.addIncome(icDto);
		
		assetRolloutIncomeDetailBO.addIncomeIdByRolloutId(rolloutDto.getId(),incomeId,OperatorDto.DEFAULT_OPERATOR);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long distributeAsset(AssetDistributeDto distributeDto,
			List<Asset> assetList) {
		ValidateUtils.notNull(distributeDto);
		String operator = distributeDto.getOperator();
		Long operatorOrgId = distributeDto.getOperatorOrgId();
		String operatorName = getWorkName(operator);
		String applyOrgName = getOrgName(operatorOrgId);
		Long stationId = distributeDto.getStationId();
		Station s = stationBO.getStationById(stationId);
		if (s == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"分发失败，服务站信息异常");
		}
		String sName = s.getName();
		
		Partner p = partnerInstanceBO.getPartnerByStationId(stationId);
		if (p == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"分发失败，合伙人信息异常");
		}
		//创建出库单
		AssetRolloutDto roDto = new AssetRolloutDto();
		roDto.setApplierWorkno(operator);
		roDto.setApplierName(operatorName);
		roDto.setStatus(AssetRolloutStatusEnum.WAIT_ROLLOUT);
		roDto.setApplierOrgId(operatorOrgId);
		roDto.setApplierOrgName(applyOrgName);
		roDto.setReceiverId(p.getTaobaoUserId().toString());
		roDto.setReceiverName(p.getName());
		roDto.setReceiverAreaType(AssetRolloutReceiverAreaTypeEnum.STATION);
		roDto.setReceiverAreaName(sName);
		roDto.setReceiverAreaId(stationId);
		roDto.setRemark("分发至 "+sName +"-" +p.getName());
		roDto.setType(AssetRolloutTypeEnum.DISTRIBUTION);
		roDto.copyOperatorDto(distributeDto);
		Long rolloutId = addRollout(roDto);
		
		// 出入库单详情  入库单id 在审批通过时 创建 并更新上去
		for (Asset asset: assetList) {
			AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
			detail.setAssetId(asset.getId());
			detail.setCategory(asset.getCategory());
			detail.setRolloutId(rolloutId);
			detail.setStatus(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN);
			detail.setType(AssetRolloutIncomeDetailTypeEnum.DISTRIBUTION);
			detail.setOperatorTime(new Date());
			detail.copyOperatorDto(distributeDto);
			assetRolloutIncomeDetailBO.addDetail(detail);
		}
		return rolloutId;
	}

	@Override
	public Long scrapAsset(AssetScrapDto scrapDto) {
		String operatorName = getWorkName(scrapDto.getOperator());
		String applyOrgName = getOrgName(scrapDto.getOperatorOrgId());
		//创建出库单
		AssetRolloutDto roDto = new AssetRolloutDto();
		roDto.setApplierWorkno(scrapDto.getOperator());
		roDto.setApplierName(operatorName);
		roDto.setStatus(AssetRolloutStatusEnum.WAIT_COMPENSATE);
		roDto.setApplierOrgId(scrapDto.getOperatorOrgId());
		roDto.setApplierOrgName(applyOrgName);
		roDto.setReceiverAreaType(AssetRolloutReceiverAreaTypeEnum.valueof(scrapDto.getScrapAreaType()));
		roDto.setReason(scrapDto.getReason());
		roDto.setReceiverAreaId(scrapDto.getOperatorOrgId());
		roDto.setTotalPayment(scrapDto.getPayment());
		if (!StringUtils.isEmpty(scrapDto.getRemark())) {
			roDto.setRemark(scrapDto.getRemark());
		}
		if (CollectionUtils.isNotEmpty(scrapDto.getAttachmentList())) {
			roDto.setAttachId(JSON.toJSONString(scrapDto.getAttachmentList()));
		}
		if (AssetRolloutReceiverAreaTypeEnum.STATION.getCode().equals(scrapDto.getScrapAreaType())) {
			Long stationId = scrapDto.getScrapAreaId();
			Station s = stationBO.getStationById(stationId);
			if (s == null) {
				throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"赔付失败，服务站信息异常");
			}
			String sName = s.getName();

			Partner p = partnerInstanceBO.getPartnerByStationId(stationId);
			if (p == null) {
				throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"赔付失败，合伙人信息异常");
			}
			roDto.setReceiverAreaName(sName);
			roDto.setReceiverId(String.valueOf(p.getTaobaoUserId()));
			roDto.setReceiverName(uicReadAdapter.getFullName(p.getTaobaoUserId()));
		} else {
			roDto.setReceiverName(operatorName);
			roDto.setReceiverAreaName(applyOrgName);
			roDto.setReceiverId(scrapDto.getOperator());
		}
		roDto.setRemark(applyOrgName+"的资产由"+operatorName+"提出赔偿申请");
		roDto.setType(AssetRolloutTypeEnum.SCRAP);
		roDto.copyOperatorDto(scrapDto);
		Long rolloutId = addRollout(roDto);
		AssetDetailDto dto = assetBO.getScrapDetailById(scrapDto.getScrapAssetId(), scrapDto);
		AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
		detail.setAssetId(dto.getId());
		detail.setCategory(dto.getCategory());
		detail.setRolloutId(rolloutId);
		detail.setType(AssetRolloutIncomeDetailTypeEnum.SCRAP);
		detail.setStatus(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN);
		detail.setPrice(dto.getPayment());
		detail.setOperatorTime(new Date());
		detail.copyOperatorDto(scrapDto);
		assetRolloutIncomeDetailBO.addDetail(detail);
		return rolloutId;
	}
	
	@Override
	public void  confirmScrapAsset(Long assetId,String operator) {
		ValidateUtils.notNull(assetId);
		ValidateUtils.notNull(operator);
		AssetRolloutIncomeDetail detail = assetRolloutIncomeDetailBO.queryWaitSignByAssetId(assetId);
		if (detail == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"赔付失败，当前资产不是待赔付资产，请核对资产信息！如有疑问，请联系资产管理员。");
		}
		//签收资产
		assetRolloutIncomeDetailBO.signAsset(detail.getId(), operator);
		//更新出入库单状态
		Long rolloutId =detail.getRolloutId();
		if (assetRolloutIncomeDetailBO.isAllSignByRolloutId(rolloutId)) {
			updateStatus(rolloutId, AssetRolloutStatusEnum.COMPENSATE_DONE, operator);
			AssetRollout ar = getRolloutById(rolloutId);
			if (AssetRolloutTypeEnum.SCRAP.getCode().equals(ar.getType())) {
				AssetAppMessageDto msgDto = new AssetAppMessageDto();
				msgDto.setMsgTypeDetail("SIGN");
				msgDto.setBizId(rolloutId);
				msgDto.setTitle("您申报的资产遗失、损毁已完成赔付，请关注！");
				msgDto.setContent(ar.getApplierOrgName() + " " +ar.getApplierName() + "的资产遗失、损毁已完成赔付，查看详情");
				List<Long>  rList = new ArrayList<Long>();
				rList.add(Long.parseLong(ar.getApplierWorkno()));
				msgDto.setReceiverList(rList);
				assetBO.sendAppMessage(msgDto);
			}
		}
	}

	@Override
	public List<AssetRollout> getDistributeAsset(Long stationId,
			Long taobaoUserId) {
		ValidateUtils.notNull(stationId);
		ValidateUtils.notNull(taobaoUserId);
		AssetRolloutExample example = new AssetRolloutExample();
		Criteria criteria = example.createCriteria();
        List<String> doList = new ArrayList<String>();
        doList.add(AssetRolloutStatusEnum.WAIT_ROLLOUT.getCode());
        doList.add(AssetRolloutStatusEnum.ROLLOUT_ING.getCode());
		criteria.andIsDeletedEqualTo("n").andReceiverAreaIdEqualTo(stationId).andReceiverIdEqualTo(String.valueOf(taobaoUserId)).andStatusIn(doList)
	  .andReceiverAreaTypeEqualTo(AssetRolloutReceiverAreaTypeEnum.STATION.getCode()).andTypeEqualTo(AssetRolloutTypeEnum.DISTRIBUTION.getCode());
		return assetRolloutMapper.selectByExample(example);
	}

	@Override
	public void deleteByRolloutId(Long rolloutId) {
		AssetRollout record = new AssetRollout();
		record.setId(rolloutId);
		DomainUtils.beforeDelete(record,"sys");
		assetRolloutMapper.updateByPrimaryKeySelective(record);
		assetRolloutIncomeDetailBO.cancelByRolloutId(rolloutId,"sys");
	}
}
