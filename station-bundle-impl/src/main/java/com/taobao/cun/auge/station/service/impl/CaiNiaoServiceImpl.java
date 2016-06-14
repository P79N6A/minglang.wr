package com.taobao.cun.auge.station.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.CaiNiaoStationDto;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.SyncAddCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncDeleteCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyCainiaoStationDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
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
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void addCainiaoStation(SyncAddCainiaoStationDto  syncAddCainiaoStationDto)
			throws AugeServiceException {
		if (syncAddCainiaoStationDto == null || syncAddCainiaoStationDto.getPartnerInstanceId() ==null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long partnerInstanceId = syncAddCainiaoStationDto.getPartnerInstanceId();
		try {
			logger.info("CaiNiaoServiceImpl addCainiaoStation partnerInstanceId : {" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				String error =  getErrorMessage("addCainiaoStation",String.valueOf(partnerInstanceId),"PartnerInstance is null");
				logger.error(error);
				throw new AugeServiceException(error);
			}
			//同步菜鸟
			CaiNiaoStationDto caiNiaoStationDto = buildCaiNiaoStationDto(instanceDto);
			
			if (PartnerInstanceTypeEnum.TPA.equals(instanceDto.getType())) {
				//淘帮手只增加关系(淘帮手需要自己的物流站点需要走审批流程)
				CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(instanceDto.getParentStationId(), CuntaoCainiaoStationRelTypeEnum.STATION);
				if(rel != null){
					caiNiaoStationDto.setStationId(rel.getCainiaoStationId());
				}
				
				//设置合伙人的uid给淘帮手
				PartnerStationRel parentPartnerRel = partnerInstanceBO.findPartnerInstanceByStationId(instanceDto.getParentStationId());
                Partner parentParner = partnerBO.getPartnerById(parentPartnerRel.getPartnerId());
                caiNiaoStationDto.setTpTaobaoUserId(parentParner.getTaobaoUserId());
		        
                caiNiaoAdapter.addStationUserRel(caiNiaoStationDto, instanceDto.getType().getCode());
			}else if (PartnerInstanceTypeEnum.TP.equals(instanceDto.getType())) {
				//合伙人
				Long caiNiaostationId =caiNiaoAdapter.addStation(caiNiaoStationDto);
				if (caiNiaostationId == null) {
				    logger.error("caiNiaoStationService.saveStation is null stationDto : {" + JSONObject.toJSONString(caiNiaoStationDto) + "}");
				    throw new RuntimeException("caiNiaoStationService.saveStation is null ");
				} 
				CuntaoCainiaoStationRelDto  relDto = new CuntaoCainiaoStationRelDto();
				relDto.setObjectId(instanceDto.getStationDto().getId());
				relDto.setCainiaoStationId(caiNiaostationId);
				relDto.setType(CuntaoCainiaoStationRelTypeEnum.STATION);
				relDto.setIsOwn("y");
				cuntaoCainiaoStationRelBO.insertCuntaoCainiaoStationRel(relDto);
			}
		} catch (Exception e) {
			String error =  getErrorMessage("addCainiaoStation",String.valueOf(partnerInstanceId),e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}
	}
	
	private CaiNiaoStationDto  buildCaiNiaoStationDto(PartnerInstanceDto instanceDto)  throws AugeServiceException{
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
         //param.setStationType(StationStationTypeConstants.COUNTRY_DSH);
		return param;
	}
	
	private Long getCountyCainiaoStationId(Long orgId) throws AugeServiceException {
		CountyStation countyStation = countyStationBO.getCountyStationByOrgId(orgId);
		if (countyStation == null) {
			String error =  getErrorMessage("getCountyCainiaoStationId",String.valueOf(orgId),"getCountyStationByOrgId is null");
			logger.error(error);
			throw new AugeServiceException(error);
		}
		Long countyStationId = countyStation.getId();
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(countyStationId, CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
		if (rel == null) {
			String error =  getErrorMessage("getCountyCainiaoStationId",String.valueOf(countyStationId),"queryCuntaoCainiaoStationRel is null");
			logger.error(error);
			throw new AugeServiceException(error);
		}
		return rel.getCainiaoStationId();
	}
	
	private String getErrorMessage(String methodName,String param,String error) {
		StringBuilder sb = new StringBuilder();
		sb.append("CaiNiaoService-Error|").append(methodName).append("(.param=").append(param).append(").").append("errorMessage:").append(error);
		return sb.toString();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateCainiaoStation(SyncModifyCainiaoStationDto  syncModifyCainiaoStationDto)
			throws AugeServiceException {
		if (syncModifyCainiaoStationDto == null || syncModifyCainiaoStationDto.getPartnerInstanceId() ==null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long partnerInstanceId = syncModifyCainiaoStationDto.getPartnerInstanceId();
		try {
			logger.info("CaiNiaoServiceImpl updateCainiaoStation partnerInstanceId : {" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				String error =  getErrorMessage("updateCainiaoStation",String.valueOf(partnerInstanceId),"PartnerInstance is null");
				logger.error(error);
				throw new AugeServiceException(error);
			}
			//同步菜鸟
			CaiNiaoStationDto caiNiaoStationDto = buildCaiNiaoStationDto(instanceDto);
			
			Long cainiaoStationId =getCainiaoStationId(instanceDto.getStationDto().getId());
			
			if (cainiaoStationId== null) {
    			if (instanceDto.getParentStationId() == null ) {
    				String error =  getErrorMessage("updateCainiaoStation",String.valueOf(partnerInstanceId),"ParentStationId is null");
    				logger.error(error);
    				throw new AugeServiceException(error);
    			}
				Long cainiaoSId = getCainiaoStationId(instanceDto.getParentStationId());
				if (cainiaoSId == null) {
					String error =  getErrorMessage("updateCainiaoStation",String.valueOf(partnerInstanceId),"ParentStationId no cainiaostation");
    				logger.error(error);
    				throw new AugeServiceException(error);
				}
				caiNiaoStationDto.setStationId(cainiaoSId);
				caiNiaoAdapter.updateStationUserRel(caiNiaoStationDto);
            }else {
	            // 同步菜鸟接口
            	caiNiaoAdapter.modifyStation(caiNiaoStationDto);
            }
		} catch (Exception e) {
			String error =  getErrorMessage("updateCainiaoStation",String.valueOf(partnerInstanceId),e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}

	}
	

	private Long getCainiaoStationId (Long stationId) throws AugeServiceException{
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
		if(rel != null){
			return rel.getCainiaoStationId();
		}
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deleteCainiaoStation(SyncDeleteCainiaoStationDto syncCainiaoStationDto)
			throws AugeServiceException {
		if (syncCainiaoStationDto == null || syncCainiaoStationDto.getPartnerInstanceId() ==null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long partnerInstanceId = syncCainiaoStationDto.getPartnerInstanceId();
		try {
			
			logger.info("CaiNiaoServiceImpl deleteCainiaoStation start,partnerInstanceId:{" + partnerInstanceId + "}");
			PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
			if (instanceDto == null) {
				String error =  getErrorMessage("deleteCainiaoStation",String.valueOf(partnerInstanceId),"PartnerInstance is null");
				logger.error(error);
				throw new AugeServiceException(error);
			}
			Long stationId = instanceDto.getStationDto().getId();
			//查询菜鸟物流站关系表
			CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
			if (rel == null || "n".equals(rel.getIsOwn())) {//没有物流站,删除关系
				CuntaoCainiaoStationRel parentRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(instanceDto.getParentStationId(), CuntaoCainiaoStationRelTypeEnum.STATION);
				if (parentRel == null) {
					String error =  getErrorMessage("deleteCainiaoStation",String.valueOf(partnerInstanceId),"parentRel is null");
					logger.error(error);
					throw new AugeServiceException(error);
				}
				caiNiaoAdapter.removeStationUserRel(instanceDto.getPartnerDto().getTaobaoUserId());
			}else {//有物流站，删除物流站
				caiNiaoAdapter.removeStationById(rel.getCainiaoStationId(), instanceDto.getPartnerDto().getTaobaoUserId());
				
				//删除本地数据菜鸟驿站对应关系
				cuntaoCainiaoStationRelBO.deleteCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
			}
		} catch (Exception e) {
			String error =  getErrorMessage("deleteCainiaoStation",String.valueOf(partnerInstanceId),e.getMessage());
			logger.error(error, e);
			throw new RuntimeException(error, e);
		}
	}
}
