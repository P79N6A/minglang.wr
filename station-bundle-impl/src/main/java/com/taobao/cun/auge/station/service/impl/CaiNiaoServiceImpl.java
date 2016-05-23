package com.taobao.cun.auge.station.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.SyncCainiaoStationDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.service.CaiNiaoService;

public class CaiNiaoServiceImpl implements CaiNiaoService {

	public static final Log logger = LogFactory.getLog(CaiNiaoServiceImpl.class);
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	@Autowired
	CaiNiaoAdapter caiNiaoAdapter;
	@Autowired
	CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	 
	@Override
	public void addCainiaoStation(SyncCainiaoStationDto syncCainiaoStationDto)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCainiaoStation(SyncCainiaoStationDto syncCainiaoStationDto)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCainiaoStation(SyncCainiaoStationDto syncCainiaoStationDto)
			throws AugeServiceException {
		if (syncCainiaoStationDto == null || syncCainiaoStationDto.getPartnerInstanceId() ==null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		Long partnerInstanceId = syncCainiaoStationDto.getPartnerInstanceId();
		logger.info("CaiNiaoServiceImpl deleteCainiaoStation start,partnerInstanceId:{" + partnerInstanceId + "}");
		PartnerInstanceDto instanceDto = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
		Long stationId = instanceDto.getStationDto().getId();
		//查询菜鸟物流站关系表
		CuntaoCainiaoStationRel rel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION.getCode());
		if (rel == null || "n".equals(rel.getIsOwn())) {//没有物流站,删除关系
			CuntaoCainiaoStationRel parentRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(instanceDto.getParentStationId(), CuntaoCainiaoStationRelTypeEnum.STATION.getCode());
			if (parentRel == null) {
				//throw AugeServiceException();
			}
		}else {//有物流站，删除物流站
			
		}
		
		
	}

	
}
