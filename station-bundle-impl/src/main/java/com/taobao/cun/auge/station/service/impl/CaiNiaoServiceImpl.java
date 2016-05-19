package com.taobao.cun.auge.station.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.SyncCainiaoStationDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.service.CaiNiaoService;

public class CaiNiaoServiceImpl implements CaiNiaoService {

	public static final Log logger = LogFactory.getLog(CaiNiaoServiceImpl.class);
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	@Autowired
	CaiNiaoAdapter caiNiaoAdapter;
	 
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
		Long stationid = instanceDto.getStationDto().getId();
		//查询菜鸟物流站关系表
		
		
		
	}

	
}
