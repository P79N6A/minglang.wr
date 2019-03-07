package com.taobao.cun.auge.station.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.cainiao.cuntaonetwork.dto.warehouse.WarehouseDTO;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.LogisticsStationApply;
import com.taobao.cun.auge.dal.domain.LogisticsStationApplyExample;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.LogisticsStationApplyMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.CaiNiaoStationDto;
import com.taobao.cun.auge.station.dto.CaiNiaoStationRelDto;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.SyncAddCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncDeleteCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyBelongTPForTpaDto;
import com.taobao.cun.auge.station.dto.SyncModifyCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyLngLatDto;
import com.taobao.cun.auge.station.dto.SyncTPDegreeCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncUpgradeToTPForTpaDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationFeatureOpModeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.CaiNiaoService;
import com.taobao.cun.common.util.BeanCopy;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("caiNiaoService")
@HSFProvider(serviceInterface= CaiNiaoService.class, clientTimeout = 8000)
public class CaiNiaoServiceImpl implements CaiNiaoService {

	public static final Log logger = LogFactory.getLog(CaiNiaoServiceImpl.class);

	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	@Autowired
	CaiNiaoAdapter caiNiaoAdapter;
	@Autowired
	CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	@Autowired
	CountyStationBO countyStationBO;
	@Autowired
	PartnerBO partnerBO;
    @Autowired
    LogisticsStationBO logisticsStationBO;
    @Autowired
    LogisticsStationApplyMapper logisticsStationApplyMapper;
    @Autowired
	StationBO stationBO;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void addCainiaoStation(SyncAddCainiaoStationDto syncAddCainiaoStationDto){
		if (syncAddCainiaoStationDto == null || syncAddCainiaoStationDto.getPartnerInstanceId() == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check error");
		}
		Long partnerInstanceId = syncAddCainiaoStationDto.getPartnerInstanceId();
			logger.info("CaiNiaoServiceImpl addCainiaoStation partnerInstanceId : {" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				String error = getErrorMessage("addCainiaoStation", String.valueOf(partnerInstanceId), "PartnerInstance is null");
				logger.error(error);
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
			}
			// 同步菜鸟
			CaiNiaoStationDto caiNiaoStationDto = buildCaiNiaoStationDto(instanceDto);

			if (PartnerInstanceTypeEnum.TPA.equals(instanceDto.getType())) {
				// 淘帮手只增加关系(淘帮手需要自己的物流站点需要走审批流程)
				CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(instanceDto.getParentStationId(),
						CuntaoCainiaoStationRelTypeEnum.STATION);
				if (rel != null) {
					caiNiaoStationDto.setStationId(rel.getCainiaoStationId());
				}

				// 设置合伙人的uid给淘帮手
				PartnerStationRel parentPartnerRel = partnerInstanceBO.findPartnerInstanceByStationId(instanceDto.getParentStationId());
				Partner parentParner = partnerBO.getPartnerById(parentPartnerRel.getPartnerId());
				caiNiaoStationDto.setTpTaobaoUserId(parentParner.getTaobaoUserId());

				caiNiaoAdapter.addStationUserRel(caiNiaoStationDto, instanceDto.getType().getCode());
			} else if (PartnerInstanceTypeEnum.TP.equals(instanceDto.getType())||PartnerInstanceTypeEnum.TPS.equals(instanceDto.getType())) {
				// 合伙人
				Long caiNiaoStationId = null;
				caiNiaoStationId = getCainiaoStationId(instanceDto.getStationDto().getId());
				if (caiNiaoStationId != null) {//入驻已有服务站
					caiNiaoStationDto.setStationId(caiNiaoStationId);
					caiNiaoAdapter.updateAdmin(caiNiaoStationDto);
				}else {
					caiNiaoStationId = caiNiaoAdapter.addStation(caiNiaoStationDto);
					if (caiNiaoStationId == null) {
						logger.error(
								"caiNiaoStationService.saveStation is null stationDto : {" + JSONObject.toJSONString(caiNiaoStationDto) + "}");
						throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"caiNiaoStationService.saveStation is null ");
					}
					CuntaoCainiaoStationRelDto relDto = new CuntaoCainiaoStationRelDto();
					relDto.setObjectId(instanceDto.getStationDto().getId());
					relDto.setCainiaoStationId(caiNiaoStationId);
					relDto.setType(CuntaoCainiaoStationRelTypeEnum.STATION);
					relDto.setIsOwn("y");
					relDto.setOperator(DomainUtils.DEFAULT_OPERATOR);
					cuntaoCainiaoStationRelBO.insertCuntaoCainiaoStationRel(relDto);
				}
			}else if (PartnerInstanceTypeEnum.TPT.equals(instanceDto.getType())) {
				// 合伙人
				Long caiNiaoStationId = null;
				caiNiaoStationId = getCainiaoStationId(instanceDto.getStationDto().getId());
				if (caiNiaoStationId != null) {//入驻已有服务站
					caiNiaoStationDto.setStationId(caiNiaoStationId);
					caiNiaoAdapter.updateAdmin(caiNiaoStationDto);
				}else {
					caiNiaoStationId = caiNiaoAdapter.addStation(caiNiaoStationDto);
					if (caiNiaoStationId == null) {
						logger.error(
								"caiNiaoStationService.saveStation is null stationDto : {" + JSONObject.toJSONString(caiNiaoStationDto) + "}");
						throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"caiNiaoStationService.saveStation is null ");
					}
					
					LinkedHashMap<String, String> featureMap = new LinkedHashMap<String, String>();
					featureMap.put(CaiNiaoAdapter.CTP_TYPE, "CtPT");
					caiNiaoAdapter.updateStationFeatures(caiNiaoStationId, featureMap);
					
					CuntaoCainiaoStationRelDto relDto = new CuntaoCainiaoStationRelDto();
					relDto.setObjectId(instanceDto.getStationDto().getId());
					relDto.setCainiaoStationId(caiNiaoStationId);
					relDto.setType(CuntaoCainiaoStationRelTypeEnum.STATION);
					relDto.setIsOwn("y");
					relDto.setOperator(DomainUtils.DEFAULT_OPERATOR);
					cuntaoCainiaoStationRelBO.insertCuntaoCainiaoStationRel(relDto);
				}
			}
	}

	private CaiNiaoStationDto buildCaiNiaoStationDto(PartnerInstanceDto instanceDto){
		StationDto stationDto = instanceDto.getStationDto();
		PartnerDto partnerDto = instanceDto.getPartnerDto();

		CaiNiaoStationDto param = new CaiNiaoStationDto();

		String stationName = "";
		if (StringUtil.isNotBlank(stationDto.getStationNum())) {
			stationName += stationDto.getStationNum();
		}
		if (StringUtil.isNotBlank(stationDto.getName())) {
			stationName += stationDto.getName();
		}
		param.setStationNum(stationDto.getStationNum());
		param.setStationName(stationName);
		param.setAlipayAccount(partnerDto.getAlipayAccount());

		param.setStationAddress(stationDto.getAddress());

		param.setContact(partnerDto.getName());
		param.setMobile(partnerDto.getMobile());
		param.setLoginId(partnerDto.getTaobaoNick());
		param.setTaobaoUserId(partnerDto.getTaobaoUserId());
		param.setApplierId(instanceDto.getApplierId());

		param.setParentId(getCountyCainiaoStationId(stationDto.getApplyOrg()));
		param.setIsOnTown(stationDto.getPartnerInstanceIsOnTown() == null ? null:stationDto.getPartnerInstanceIsOnTown().getCode());
		// param.setStationType(StationStationTypeConstants.COUNTRY_DSH);
		return param;
	}

	private Long getCountyCainiaoStationId(Long orgId){
		CountyStation countyStation = countyStationBO.getCountyStationByOrgId(orgId);
		if (countyStation == null) {
			String error = getErrorMessage("getCountyCainiaoStationId", String.valueOf(orgId), "getCountyStationByOrgId is null");
			logger.error(error);
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
		}
		Long countyStationId = countyStation.getId();
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(countyStationId,
				CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
		if (rel == null) {
			String error = getErrorMessage("getCountyCainiaoStationId", String.valueOf(countyStationId),
					"queryCuntaoCainiaoStationRel is null");
			logger.error(error);
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
		}
		return rel.getCainiaoStationId();
	}

	private String getErrorMessage(String methodName, String param, String error) {
		StringBuilder sb = new StringBuilder();
		sb.append("CaiNiaoService-Error|").append(methodName).append("(.param=").append(param).append(").").append("errorMessage:")
				.append(error);
		return sb.toString();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateCainiaoStation(SyncModifyCainiaoStationDto syncModifyCainiaoStationDto){
		if (syncModifyCainiaoStationDto == null || syncModifyCainiaoStationDto.getPartnerInstanceId() == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check error");
		}
		Long partnerInstanceId = syncModifyCainiaoStationDto.getPartnerInstanceId();
			logger.info("CaiNiaoServiceImpl updateCainiaoStation partnerInstanceId : {" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				String error = getErrorMessage("updateCainiaoStation", String.valueOf(partnerInstanceId), "PartnerInstance is null");
				logger.error(error);
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
			}
			// 同步菜鸟
			CaiNiaoStationDto caiNiaoStationDto = buildCaiNiaoStationDto(instanceDto);

			Long cainiaoStationId = getCainiaoStationId(instanceDto.getStationDto().getId());

			if (cainiaoStationId == null) {
				if (instanceDto.getParentStationId() == null) {
					String error = getErrorMessage("updateCainiaoStation", String.valueOf(partnerInstanceId), "ParentStationId is null");
					logger.error(error);
					throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
				}
				Long cainiaoSId = getCainiaoStationId(instanceDto.getParentStationId());
				if (cainiaoSId == null) {
					String error = getErrorMessage("updateCainiaoStation", String.valueOf(partnerInstanceId),
							"ParentStationId no cainiaostation");
					logger.error(error);
					throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
				}
				caiNiaoStationDto.setStationId(cainiaoSId);
				caiNiaoAdapter.updateStationUserRel(caiNiaoStationDto);
			} else {
				// 同步菜鸟接口
				caiNiaoStationDto.setStationId(cainiaoStationId);
				caiNiaoAdapter.modifyStation(caiNiaoStationDto);
			}
	}

	private Long getCainiaoStationId(Long stationId){
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
				CuntaoCainiaoStationRelTypeEnum.STATION);
		if (rel != null) {
			return rel.getCainiaoStationId();
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deleteCainiaoStation(SyncDeleteCainiaoStationDto syncCainiaoStationDto){
		if (syncCainiaoStationDto == null || syncCainiaoStationDto.getPartnerInstanceId() == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check error");
		}
		Long partnerInstanceId = syncCainiaoStationDto.getPartnerInstanceId();
			logger.info("CaiNiaoServiceImpl deleteCainiaoStation start,partnerInstanceId:{" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				String error = getErrorMessage("deleteCainiaoStation", String.valueOf(partnerInstanceId), "PartnerInstance is null");
				logger.error(error);
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
			}
			Long stationId = instanceDto.getStationDto().getId();
			// 查询菜鸟物流站关系表
			CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
					CuntaoCainiaoStationRelTypeEnum.STATION);
			if (rel == null || "n".equals(rel.getIsOwn())) {// 没有物流站,删除关系
				CuntaoCainiaoStationRel parentRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(instanceDto.getParentStationId(),
						CuntaoCainiaoStationRelTypeEnum.STATION);
				if (parentRel == null) {
					String error = getErrorMessage("deleteCainiaoStation", String.valueOf(partnerInstanceId), "parentRel is null");
					logger.error(error);
					throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
				}
				caiNiaoAdapter.removeStationUserRel(instanceDto.getPartnerDto().getTaobaoUserId());
			} else {// 有物流站，删除物流站
				caiNiaoAdapter.removeStationById(rel.getCainiaoStationId(), instanceDto.getPartnerDto().getTaobaoUserId());
				
				//删除logistics_station
				Long logisId = rel.getLogisticsStationId();
				if (logisId != null) {
					logisticsStationBO.changeState(logisId, syncCainiaoStationDto.getOperator(), "QUIT");
				}
				// 删除本地数据菜鸟驿站对应关系
				cuntaoCainiaoStationRelBO.deleteCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
			}
	}
	
	@Override
	public void deleteNotUsedCainiaoStation(Long stationId){
		if (stationId == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check error");
		}
			logger.info("CaiNiaoServiceImpl deleteNotUserdCainiaoStation start,stationId:{" + stationId + "}");
			// 查询菜鸟物流站关系表
			CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
					CuntaoCainiaoStationRelTypeEnum.STATION);
			if (rel == null || "n".equals(rel.getIsOwn())) {// 没有物流站,删除关系
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"cainiaoStationRel is not exist");
			} else {// 有物流站，删除物流站
				caiNiaoAdapter.removeNotUserdStationById(rel.getCainiaoStationId());
				
				//删除logistics_station
				Long logisId = rel.getLogisticsStationId();
				if (logisId != null) {
					logisticsStationBO.changeState(logisId, OperatorDto.defaultOperator().getOperator(), "QUIT");
				}
				// 删除本地数据菜鸟驿站对应关系
				cuntaoCainiaoStationRelBO.deleteCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
			}
	}

	@Override
	public void updateCainiaoStationFeatureForTPDegree(SyncTPDegreeCainiaoStationDto syncTPDegreeCainiaoStationDto) {
		Long parentStationId = syncTPDegreeCainiaoStationDto.getParentStationId();
		Long stationId = syncTPDegreeCainiaoStationDto.getStationId();
		Long parentTaobaoUserId = syncTPDegreeCainiaoStationDto.getParentTaobaoUserId();
		Long taobaoUserId = syncTPDegreeCainiaoStationDto.getTaobaoUserId();
		if (parentStationId == null || stationId == null || parentTaobaoUserId == null) {
			logger.error("addCNStationFeature exception parameters is null!stationId:[" + stationId + "],partnerStationId["
					+ parentStationId + "],partnerTaobaoUserId[" + parentTaobaoUserId + "]");
			return;
		}
		Long cnStationId = cuntaoCainiaoStationRelBO.getCainiaoStationId(stationId);
		if (cnStationId != null) {
			// 调用新菜鸟接口
			LinkedHashMap<String, String> featureMap = new LinkedHashMap<String, String>();
			featureMap.put(CaiNiaoAdapter.CTP_TB_UID, parentTaobaoUserId.toString());
			featureMap.put(CaiNiaoAdapter.CTP_ORG_STA_ID, parentStationId.toString());
			featureMap.put(CaiNiaoAdapter.CTP_TYPE, "CtPA1_0");
			caiNiaoAdapter.updateStationFeatures(cnStationId, featureMap);

			LinkedHashMap<String, String> featureMap1 = new LinkedHashMap<String, String>();
			featureMap1.put(CaiNiaoAdapter.PARTNER_ID, parentTaobaoUserId.toString());
			caiNiaoAdapter.updateStationUserRelFeature(taobaoUserId, featureMap1);
		}

	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void unBindAdmin(Long stationId){
		if (stationId == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check error");
		}
		Long cnStationId = cuntaoCainiaoStationRelBO.getCainiaoStationId(stationId);
		if (cnStationId == null) {
			String error = getErrorMessage("unBindAdmin", String.valueOf(stationId), "cnStationId is null");
			logger.error(error);
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
		}
			boolean res  = caiNiaoAdapter.unBindAdmin(cnStationId);
			if (!res) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"res is false");
			}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void bindAdmin(SyncAddCainiaoStationDto syncAddCainiaoStationDto){
		if (syncAddCainiaoStationDto == null || syncAddCainiaoStationDto.getPartnerInstanceId() == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check error");
		}
		Long partnerInstanceId = syncAddCainiaoStationDto.getPartnerInstanceId();
			logger.info("CaiNiaoServiceImpl bindAdmin partnerInstanceId : {" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "partnerInstance is null");
			}
			// 同步菜鸟
			CaiNiaoStationDto caiNiaoStationDto = buildCaiNiaoStationDto(instanceDto);
			Long cainiaoStationId = getCainiaoStationId(instanceDto.getStationDto().getId());
			caiNiaoStationDto.setStationId(cainiaoStationId);
			caiNiaoAdapter.bindAdmin(caiNiaoStationDto);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateAdmin(
			SyncAddCainiaoStationDto syncAddCainiaoStationDto)
			{
		if (syncAddCainiaoStationDto == null || syncAddCainiaoStationDto.getPartnerInstanceId() ==null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check error");
		}
		Long partnerInstanceId = syncAddCainiaoStationDto.getPartnerInstanceId();
		
			logger.info("CaiNiaoServiceImpl updateAdmin partnerInstanceId : {" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "partnerInstance is null");
			}
			Long stationId = instanceDto.getStationDto().getId();
			Long cainiaoStationId = getCainiaoStationId(stationId);
			CaiNiaoStationDto caiNiaoStationDto = buildCaiNiaoStationDto(instanceDto);
			caiNiaoStationDto.setStationId(cainiaoStationId);
			caiNiaoAdapter.updateAdmin(caiNiaoStationDto);
	}

	@Override
	public void updateBelongTPForTpa(
			SyncModifyBelongTPForTpaDto syncModifyBelongTPForTpaDto)
			{
		if (syncModifyBelongTPForTpaDto == null || syncModifyBelongTPForTpaDto.getPartnerInstanceId() ==null
				|| syncModifyBelongTPForTpaDto.getParentPartnerInstanceId() == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check error");
		}
		Long partnerInstanceId = syncModifyBelongTPForTpaDto.getPartnerInstanceId();
		Long parentPartnerInstanceId = syncModifyBelongTPForTpaDto.getParentPartnerInstanceId();
			logger.info("CaiNiaoServiceImpl updateBelongTPForTpa partnerInstanceId : {" + partnerInstanceId + "}"+"parentPartnerInstanceId:{"+parentPartnerInstanceId+"}");
			PartnerInstanceDto tpaInstanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (tpaInstanceDto == null) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "tpaInstanceDto is null");
			}
			PartnerInstanceDto parentInstanceDto = partnerInstanceBO.getPartnerInstanceById(parentPartnerInstanceId);
			if (parentInstanceDto == null) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "parentInstanceDto is null");
			}
			
			Long tpaStationId = tpaInstanceDto.getStationDto().getId();
			Long parentStationId = parentInstanceDto.getStationDto().getId();
			Long cainiaoStationId = getCainiaoStationId(tpaStationId);
			if (cainiaoStationId != null) {//淘帮手有独立物流地址
				LinkedHashMap<String, String> featureMap = new LinkedHashMap<String, String>();
				featureMap.put(CaiNiaoAdapter.CTP_TB_UID, String.valueOf(parentInstanceDto.getTaobaoUserId()));
				featureMap.put(CaiNiaoAdapter.CTP_ORG_STA_ID, String.valueOf(parentStationId));
				featureMap.put(CaiNiaoAdapter.CTP_TYPE, "CtPA1_0");
				caiNiaoAdapter.updateStationFeatures(cainiaoStationId, featureMap);
				
				LinkedHashMap<String, String> featureMap1 = new LinkedHashMap<String, String>();
				featureMap1.put(CaiNiaoAdapter.PARTNER_ID, String.valueOf(parentInstanceDto.getTaobaoUserId()));
				caiNiaoAdapter.updateStationUserRelFeature(tpaInstanceDto.getTaobaoUserId(), featureMap1);
			}else {//淘帮手没有独立物流地址
				CaiNiaoStationDto caiNiaoStationDto = buildCaiNiaoStationDto(tpaInstanceDto);
				Long cainiaoSId = getCainiaoStationId(parentInstanceDto.getStationId());
				if (cainiaoSId == null) {
					String error = getErrorMessage("updateBelongTPForTpa", String.valueOf(partnerInstanceId),
							"ParentStationId no cainiaostation");
					logger.error(error);
					throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
				}
				caiNiaoStationDto.setStationId(cainiaoSId);
				caiNiaoAdapter.updateStationUserRel(caiNiaoStationDto);
				
				LinkedHashMap<String, String> featureMap1 = new LinkedHashMap<String, String>();
				featureMap1.put(CaiNiaoAdapter.PARTNER_ID, String.valueOf(parentInstanceDto.getTaobaoUserId()));
				caiNiaoAdapter.updateStationUserRelFeature(tpaInstanceDto.getTaobaoUserId(), featureMap1);
			}
	}
	
	@Override
	public void upgradeToTPForTpa(SyncUpgradeToTPForTpaDto syncUpgradeToTPForTpaDto){
		if (syncUpgradeToTPForTpaDto == null || 
				syncUpgradeToTPForTpaDto.getPartnerInstanceId() == null ||
				syncUpgradeToTPForTpaDto.getOldPartnerInstanceId()== null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check error");
		}
		Long piId = syncUpgradeToTPForTpaDto.getPartnerInstanceId();//新的实例id
		Long oldPiId = syncUpgradeToTPForTpaDto.getOldPartnerInstanceId();//老的淘帮手实例id
			logger.info("CaiNiaoServiceImpl upgradeToTPForTpa start,oldPiId=" + oldPiId + " piId="+piId);
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(oldPiId);
			if (instanceDto == null) {
				String error = getErrorMessage("upgradeToTPForTpa", String.valueOf(oldPiId), "PartnerInstance is null");
				logger.error(error);
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
			}
			Long stationId = instanceDto.getStationId();
			Long taobaoUserId = instanceDto.getTaobaoUserId();
			
			// 查询菜鸟物流站关系表
			CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
					CuntaoCainiaoStationRelTypeEnum.STATION);
			if (rel == null || "n".equals(rel.getIsOwn())) {// 没有物流站,删除关系
				CuntaoCainiaoStationRel parentRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(instanceDto.getParentStationId(),
						CuntaoCainiaoStationRelTypeEnum.STATION);
				if (parentRel == null) {
					String error = getErrorMessage("deleteCainiaoStation", String.valueOf(oldPiId), "parentRel is null");
					logger.error(error);
					throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
				}
				//删除老的淘帮手的物流站关系
				caiNiaoAdapter.removeStationUserRel(taobaoUserId);
				
				//新增合伙人服务站
				SyncAddCainiaoStationDto syncAddCainiaoStationDto = new SyncAddCainiaoStationDto();
				syncAddCainiaoStationDto.setPartnerInstanceId(piId);
				addCainiaoStation(syncAddCainiaoStationDto);
				
			} else {// 有物流站,更新物流站信息
				SyncModifyCainiaoStationDto syncModifyCainiaoStationDto = new SyncModifyCainiaoStationDto();
				syncModifyCainiaoStationDto.setPartnerInstanceId(piId);
				updateCainiaoStation(syncModifyCainiaoStationDto);
				
				Set<String> featureKey = new HashSet<String>();
				featureKey.add(CaiNiaoAdapter.CTP_TB_UID);
				featureKey.add(CaiNiaoAdapter.CTP_ORG_STA_ID);
				featureKey.add(CaiNiaoAdapter.CTP_TYPE);
				caiNiaoAdapter.removeStationFeatures(rel.getCainiaoStationId(), featureKey);
				
				// 调用新菜鸟接口
				LinkedHashMap<String, String> featureMap = new LinkedHashMap<String, String>();
				featureMap.put(CaiNiaoAdapter.CTP_TYPE, "CtP");
				caiNiaoAdapter.updateStationFeatures(rel.getCainiaoStationId(), featureMap);
				
				LinkedHashMap<String, String> featureMap1 = new LinkedHashMap<String, String>();
				featureMap1.put(CaiNiaoAdapter.PARTNER_ID, String.valueOf(taobaoUserId));
				caiNiaoAdapter.updateStationUserRelFeature(taobaoUserId, featureMap1);
			}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void closeCainiaoStationForTpa(Long partnerInstanceId, OperatorDto operatorDto){
		if (partnerInstanceId == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check error");
		}
		PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
		Long stationId = instanceDto.getStationId();
		// 查询菜鸟物流站关系表
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
				CuntaoCainiaoStationRelTypeEnum.STATION);
		
		if (rel == null || "n".equals(rel.getIsOwn())) {// 没有物流站,删除关系
			String error = getErrorMessage("closeCainiaoStationForTpa", String.valueOf(stationId), "CuntaoCainiaoStationRel is null");
			logger.error(error);
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
		}else {// 有物流站,新删除物流站，再绑定村小二物流站
			caiNiaoAdapter.removeStationById(rel.getCainiaoStationId(), instanceDto.getPartnerDto().getTaobaoUserId());
			
			//删除logistics_station
			Long logisId = rel.getLogisticsStationId();
			if (logisId != null) {
				logisticsStationBO.delete(logisId, operatorDto.getOperator());
				deleteLogisticsStationApply(logisId, operatorDto.getOperator());
			}
			// 删除本地数据菜鸟驿站对应关系
			cuntaoCainiaoStationRelBO.deleteCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
			
			//绑定村小二物流站
			SyncAddCainiaoStationDto syncAddCainiaoStationDto = new SyncAddCainiaoStationDto();
			syncAddCainiaoStationDto.setPartnerInstanceId(partnerInstanceId);
			syncAddCainiaoStationDto.copyOperatorDto(operatorDto);
			addCainiaoStation(syncAddCainiaoStationDto);
		}
	}
	
	private void deleteLogisticsStationApply(Long logisticsStationId, String operator) {
		LogisticsStationApply record = new LogisticsStationApply();
		
		record.setModifer(operator);
		record.setGmtModified(new Date());
		record.setIsDeleted("y");

		LogisticsStationApplyExample example = new LogisticsStationApplyExample();

		example.createCriteria().andIsDeletedEqualTo("n").andLogisticsStationIdEqualTo(logisticsStationId).andTypeEqualTo("applyLogistics");

		logisticsStationApplyMapper.updateByExampleSelective(record, example);
	}

	/** 停业通知菜鸟 */
	@Override
    public void closeCainiaoStation(SyncModifyCainiaoStationDto syncModifyCainiaoStationDto){
		PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(syncModifyCainiaoStationDto.getPartnerInstanceId());
		Long stationId = instanceDto.getStationId();
		// 查询菜鸟物流站关系表
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
				CuntaoCainiaoStationRelTypeEnum.STATION);
		if (rel != null) {// 没有物流站
			caiNiaoAdapter.closeToCainiaoStation(rel.getCainiaoStationId());
		}
	}

	/** 经纬度修改同步菜鸟 */
	@Override
    public void modifyLngLatToCainiao(SyncModifyLngLatDto syncModifyLngLatDto){
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(syncModifyLngLatDto.getPartnerInstanceId());
			Long stationId = instanceDto.getStationId();
			// 查询菜鸟物流站关系表
			CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
					CuntaoCainiaoStationRelTypeEnum.STATION);
			if (rel != null) {// 有物流站才同步
				syncModifyLngLatDto.setCainiaoStationId(rel.getCainiaoStationId());
				caiNiaoAdapter.modifyLngLatToCainiao(syncModifyLngLatDto);
			}
	}

	@Override
	public void updateCainiaoStationFeature(List<Long> cainiaoStationIds) {
		for(Long cainiaoStationId:cainiaoStationIds){
			Set<String> featureKey = new HashSet<String>();
			featureKey.add(CaiNiaoAdapter.CTP_TYPE);
			caiNiaoAdapter.removeStationFeatures(cainiaoStationId, featureKey);
		}
	}

	//无县仓的村点feature同步菜鸟服务,opMode:y新增 n删除
	@Override
    public void synNoWarehouseStationFeature(Long cainiaoStationId,StationFeatureOpModeEnum opMode) {
        if(cainiaoStationId == null || opMode == null){
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"param check is null"); 
        }
        try {
            LinkedHashMap<String, String> features = new LinkedHashMap<String, String>();
            features.put("noWarehouseSta",opMode.getDesc());
            caiNiaoAdapter.updateStationFeatures(cainiaoStationId, features);
        } catch (Exception e) {
            logger.error("synNoWarehouseStationFeature get error:" + e.getMessage());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"get result has error"); 
        }
    }

	@Override
	public void addCainiaoStationFeature(Long stationId, String featureKey, String value) {
		// 查询菜鸟物流站关系表
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
				CuntaoCainiaoStationRelTypeEnum.STATION);
		if (rel != null) {// 有物流站才同步
			LinkedHashMap<String,String> features =new LinkedHashMap<String,String>();
			features.put(featureKey, value);
			caiNiaoAdapter.updateStationFeatures(rel.getCainiaoStationId(), features);
		}
		
	}

	@Override
	public void removeCainiaoStationFeature(Long stationId, String featureKey) {
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
				CuntaoCainiaoStationRelTypeEnum.STATION);
		if (rel != null) {
			Set<String> key = new HashSet<String>();
			key.add(featureKey);
			caiNiaoAdapter.removeStationFeatures(rel.getCainiaoStationId(), key);
		}
	}

	@Override
	public CaiNiaoStationRelDto getCaiNiaoStationRelByStation(Long stationId) {
		CuntaoCainiaoStationRel cuntaoCainiaoStationRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
		if(cuntaoCainiaoStationRel == null) {
			return null;
		}
		
		return BeanCopy.copy(CaiNiaoStationRelDto.class, cuntaoCainiaoStationRel);
	}

	@Override
	public Boolean checkCainiaoCountyIsOperating(Long stationId) {
		Station s = stationBO.getStationById(stationId);
		if (s == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"station is null"); 
		}
		Long cnCountyId = getCountyCainiaoStationId(s.getApplyOrg());
		if (cnCountyId == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"cnCountyId is null"); 
		}
		
		WarehouseDTO w = caiNiaoAdapter.queryWarehouseByCainiaoCountyId(cnCountyId);
		if (!w.isUse()) {
			return false;
		}
		
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
				CuntaoCainiaoStationRelTypeEnum.STATION);
		if (rel==null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"cainiaostation is null"); 
		}
		 Map<String,String>  m = caiNiaoAdapter.getFeatureByCainiaoStationId(rel.getCainiaoStationId());
		 if (m.containsKey("noWarehouseSta") && "y".equals(m.get("noWarehouseSta"))) {
			 return false;
		 }
		return true;
	}
}
