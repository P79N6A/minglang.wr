package com.taobao.cun.auge.station.service.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
import com.taobao.cun.auge.dal.mapper.LogisticsStationApplyMapper;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.CaiNiaoStationDto;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.SyncAddCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncDeleteCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyBelongTPForTpaDto;
import com.taobao.cun.auge.station.dto.SyncModifyCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncTPDegreeCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncUpgradeToTPForTpaDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.service.CaiNiaoService;

@Service("caiNiaoService")
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

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void addCainiaoStation(SyncAddCainiaoStationDto syncAddCainiaoStationDto) throws AugeServiceException {
		if (syncAddCainiaoStationDto == null || syncAddCainiaoStationDto.getPartnerInstanceId() == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long partnerInstanceId = syncAddCainiaoStationDto.getPartnerInstanceId();
		try {
			logger.info("CaiNiaoServiceImpl addCainiaoStation partnerInstanceId : {" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				String error = getErrorMessage("addCainiaoStation", String.valueOf(partnerInstanceId), "PartnerInstance is null");
				logger.error(error);
				throw new AugeServiceException(error);
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
			} else if (PartnerInstanceTypeEnum.TP.equals(instanceDto.getType())) {
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
						throw new RuntimeException("caiNiaoStationService.saveStation is null ");
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
						throw new RuntimeException("caiNiaoStationService.saveStation is null ");
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
		} catch (Exception e) {
			String error = getErrorMessage("addCainiaoStation", String.valueOf(partnerInstanceId), e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}
	}

	private CaiNiaoStationDto buildCaiNiaoStationDto(PartnerInstanceDto instanceDto) throws AugeServiceException {
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

	private Long getCountyCainiaoStationId(Long orgId) throws AugeServiceException {
		CountyStation countyStation = countyStationBO.getCountyStationByOrgId(orgId);
		if (countyStation == null) {
			String error = getErrorMessage("getCountyCainiaoStationId", String.valueOf(orgId), "getCountyStationByOrgId is null");
			logger.error(error);
			throw new AugeServiceException(error);
		}
		Long countyStationId = countyStation.getId();
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(countyStationId,
				CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
		if (rel == null) {
			String error = getErrorMessage("getCountyCainiaoStationId", String.valueOf(countyStationId),
					"queryCuntaoCainiaoStationRel is null");
			logger.error(error);
			throw new AugeServiceException(error);
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
	public void updateCainiaoStation(SyncModifyCainiaoStationDto syncModifyCainiaoStationDto) throws AugeServiceException {
		if (syncModifyCainiaoStationDto == null || syncModifyCainiaoStationDto.getPartnerInstanceId() == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long partnerInstanceId = syncModifyCainiaoStationDto.getPartnerInstanceId();
		try {
			logger.info("CaiNiaoServiceImpl updateCainiaoStation partnerInstanceId : {" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				String error = getErrorMessage("updateCainiaoStation", String.valueOf(partnerInstanceId), "PartnerInstance is null");
				logger.error(error);
				throw new AugeServiceException(error);
			}
			// 同步菜鸟
			CaiNiaoStationDto caiNiaoStationDto = buildCaiNiaoStationDto(instanceDto);

			Long cainiaoStationId = getCainiaoStationId(instanceDto.getStationDto().getId());

			if (cainiaoStationId == null) {
				if (instanceDto.getParentStationId() == null) {
					String error = getErrorMessage("updateCainiaoStation", String.valueOf(partnerInstanceId), "ParentStationId is null");
					logger.error(error);
					throw new AugeServiceException(error);
				}
				Long cainiaoSId = getCainiaoStationId(instanceDto.getParentStationId());
				if (cainiaoSId == null) {
					String error = getErrorMessage("updateCainiaoStation", String.valueOf(partnerInstanceId),
							"ParentStationId no cainiaostation");
					logger.error(error);
					throw new AugeServiceException(error);
				}
				caiNiaoStationDto.setStationId(cainiaoSId);
				caiNiaoAdapter.updateStationUserRel(caiNiaoStationDto);
			} else {
				// 同步菜鸟接口
				caiNiaoStationDto.setStationId(cainiaoStationId);
				caiNiaoAdapter.modifyStation(caiNiaoStationDto);
			}
		} catch (Exception e) {
			String error = getErrorMessage("updateCainiaoStation", String.valueOf(partnerInstanceId), e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}

	}

	private Long getCainiaoStationId(Long stationId) throws AugeServiceException {
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
				CuntaoCainiaoStationRelTypeEnum.STATION);
		if (rel != null) {
			return rel.getCainiaoStationId();
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deleteCainiaoStation(SyncDeleteCainiaoStationDto syncCainiaoStationDto) throws AugeServiceException {
		if (syncCainiaoStationDto == null || syncCainiaoStationDto.getPartnerInstanceId() == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long partnerInstanceId = syncCainiaoStationDto.getPartnerInstanceId();
		try {

			logger.info("CaiNiaoServiceImpl deleteCainiaoStation start,partnerInstanceId:{" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				String error = getErrorMessage("deleteCainiaoStation", String.valueOf(partnerInstanceId), "PartnerInstance is null");
				logger.error(error);
				throw new AugeServiceException(error);
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
					throw new AugeServiceException(error);
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
		} catch (Exception e) {
			String error = getErrorMessage("deleteCainiaoStation", String.valueOf(partnerInstanceId), e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}
	}
	
	@Override
	public void deleteNotUsedCainiaoStation(Long stationId) throws AugeServiceException {
		if (stationId == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		try {
			logger.info("CaiNiaoServiceImpl deleteNotUserdCainiaoStation start,stationId:{" + stationId + "}");
			// 查询菜鸟物流站关系表
			CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
					CuntaoCainiaoStationRelTypeEnum.STATION);
			if (rel == null || "n".equals(rel.getIsOwn())) {// 没有物流站,删除关系
				throw new AugeServiceException(CommonExceptionEnum.RECORD_EXISTS);
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
		} catch (Exception e) {
			String error = getErrorMessage("deleteNotUserdCainiaoStation", String.valueOf(stationId), e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
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
	public void unBindAdmin(Long stationId) throws AugeServiceException {
		if (stationId == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long cnStationId = cuntaoCainiaoStationRelBO.getCainiaoStationId(stationId);
		if (cnStationId == null) {
			String error = getErrorMessage("unBindAdmin", String.valueOf(stationId), "cnStationId is null");
			logger.error(error);
			throw new AugeServiceException(error);
		}
		try {
			boolean res  = caiNiaoAdapter.unBindAdmin(cnStationId);
			if (!res) {
				throw new RuntimeException("res is false");
			}
		} catch (Exception e) {
			String error = getErrorMessage("unBindAdmin", String.valueOf(stationId), e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void bindAdmin(SyncAddCainiaoStationDto syncAddCainiaoStationDto) throws AugeServiceException {
		if (syncAddCainiaoStationDto == null || syncAddCainiaoStationDto.getPartnerInstanceId() == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long partnerInstanceId = syncAddCainiaoStationDto.getPartnerInstanceId();
		try {
			logger.info("CaiNiaoServiceImpl bindAdmin partnerInstanceId : {" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				throw new AugeServiceException( "partnerInstance is null");
			}
			// 同步菜鸟
			CaiNiaoStationDto caiNiaoStationDto = buildCaiNiaoStationDto(instanceDto);
			Long cainiaoStationId = getCainiaoStationId(instanceDto.getStationDto().getId());
			caiNiaoStationDto.setStationId(cainiaoStationId);
			caiNiaoAdapter.bindAdmin(caiNiaoStationDto);
		} catch (Exception e) {
			String error = getErrorMessage("bindAdmin", String.valueOf(partnerInstanceId), e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateAdmin(
			SyncAddCainiaoStationDto syncAddCainiaoStationDto)
			throws AugeServiceException {
		if (syncAddCainiaoStationDto == null || syncAddCainiaoStationDto.getPartnerInstanceId() ==null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long partnerInstanceId = syncAddCainiaoStationDto.getPartnerInstanceId();
		
		try {
			logger.info("CaiNiaoServiceImpl updateAdmin partnerInstanceId : {" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				throw new AugeServiceException( "partnerInstance is null");
			}
			Long stationId = instanceDto.getStationDto().getId();
			Long cainiaoStationId = getCainiaoStationId(stationId);
			CaiNiaoStationDto caiNiaoStationDto = buildCaiNiaoStationDto(instanceDto);
			caiNiaoStationDto.setStationId(cainiaoStationId);
			caiNiaoAdapter.updateAdmin(caiNiaoStationDto);
		} catch (Exception e) {
			String error = getErrorMessage("updateAdmin", String.valueOf(partnerInstanceId), e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}
	}

	@Override
	public void updateBelongTPForTpa(
			SyncModifyBelongTPForTpaDto syncModifyBelongTPForTpaDto)
			throws AugeServiceException {
		if (syncModifyBelongTPForTpaDto == null || syncModifyBelongTPForTpaDto.getPartnerInstanceId() ==null
				|| syncModifyBelongTPForTpaDto.getParentPartnerInstanceId() == null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long partnerInstanceId = syncModifyBelongTPForTpaDto.getPartnerInstanceId();
		Long parentPartnerInstanceId = syncModifyBelongTPForTpaDto.getParentPartnerInstanceId();
		try {
			logger.info("CaiNiaoServiceImpl updateBelongTPForTpa partnerInstanceId : {" + partnerInstanceId + "}"+"parentPartnerInstanceId:{"+parentPartnerInstanceId+"}");
			PartnerInstanceDto tpaInstanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (tpaInstanceDto == null) {
				throw new AugeServiceException( "tpaInstanceDto is null");
			}
			PartnerInstanceDto parentInstanceDto = partnerInstanceBO.getPartnerInstanceById(parentPartnerInstanceId);
			if (parentInstanceDto == null) {
				throw new AugeServiceException( "parentInstanceDto is null");
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
					throw new AugeServiceException(error);
				}
				caiNiaoStationDto.setStationId(cainiaoSId);
				caiNiaoAdapter.updateStationUserRel(caiNiaoStationDto);
				
				LinkedHashMap<String, String> featureMap1 = new LinkedHashMap<String, String>();
				featureMap1.put(CaiNiaoAdapter.PARTNER_ID, String.valueOf(parentInstanceDto.getTaobaoUserId()));
				caiNiaoAdapter.updateStationUserRelFeature(tpaInstanceDto.getTaobaoUserId(), featureMap1);
			}
		} catch (Exception e) {
			String error = getErrorMessage("updateBelongTPForTpa", "partnerInstanceId:" + partnerInstanceId +
		" parentPartnerInstanceId:"+parentPartnerInstanceId, e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}
	}
	
	@Override
	public void upgradeToTPForTpa(SyncUpgradeToTPForTpaDto syncUpgradeToTPForTpaDto)throws AugeServiceException {
		if (syncUpgradeToTPForTpaDto == null || 
				syncUpgradeToTPForTpaDto.getPartnerInstanceId() == null ||
				syncUpgradeToTPForTpaDto.getOldPartnerInstanceId()== null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long piId = syncUpgradeToTPForTpaDto.getPartnerInstanceId();//新的实例id
		Long oldPiId = syncUpgradeToTPForTpaDto.getOldPartnerInstanceId();//老的淘帮手实例id
		try {

			logger.info("CaiNiaoServiceImpl upgradeToTPForTpa start,oldPiId=" + oldPiId + " piId="+piId);
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(oldPiId);
			if (instanceDto == null) {
				String error = getErrorMessage("upgradeToTPForTpa", String.valueOf(oldPiId), "PartnerInstance is null");
				logger.error(error);
				throw new AugeServiceException(error);
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
					throw new AugeServiceException(error);
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
		} catch (Exception e) {
			String error = getErrorMessage("upgradeToTPForTpa", "oldPiId=" + oldPiId + " piId="+piId, e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void closeCainiaoStationForTpa(Long partnerInstanceId, OperatorDto operatorDto)throws AugeServiceException {
		if (partnerInstanceId == null) {
			throw new AugeSystemException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
		Long stationId = instanceDto.getStationId();
		// 查询菜鸟物流站关系表
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId,
				CuntaoCainiaoStationRelTypeEnum.STATION);
		
		if (rel == null || "n".equals(rel.getIsOwn())) {// 没有物流站,删除关系
			String error = getErrorMessage("closeCainiaoStationForTpa", String.valueOf(stationId), "CuntaoCainiaoStationRel is null");
			logger.error(error);
			throw new AugeServiceException(error);
		}else {// 有物流站,新删除物流站，再绑定村小二物流站
			caiNiaoAdapter.removeStationById(rel.getCainiaoStationId(), instanceDto.getPartnerDto().getTaobaoUserId());
			
			//删除logistics_station
			Long logisId = rel.getLogisticsStationId();
			if (logisId != null) {
				logisticsStationBO.changeState(logisId, operatorDto.getOperator(), "QUIT");
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
		try {
			LogisticsStationApply record = new LogisticsStationApply();
			DomainUtils.beforeDelete(record, operator);

			LogisticsStationApplyExample example = new LogisticsStationApplyExample();

			example.createCriteria().andIsDeletedEqualTo("y").andLogisticsStationIdEqualTo(logisticsStationId);

			logisticsStationApplyMapper.updateByExampleSelective(record, example);
		} catch (Exception e) {
			throw new AugeServiceException("删除物流申请单失败");
		}
	}
}
