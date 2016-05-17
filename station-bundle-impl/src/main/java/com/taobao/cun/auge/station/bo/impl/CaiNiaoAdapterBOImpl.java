package com.taobao.cun.auge.station.bo.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
//import com.alibaba.cainiao.cuntaonetwork.service.station.StationUserWriteService;
//import com.alibaba.cainiao.cuntaonetwork.service.station.StationWriteService;
//import com.alibaba.cainiao.cuntaonetwork.service.warehouse.CountyDomainWriteService;
import com.taobao.cun.auge.station.bo.CaiNiaoAdapterBO;
import com.taobao.cun.auge.station.dto.CaiNiaoStationDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
//import com.taobao.cun.biz.hr.bo.Emp360BO;
//import com.taobao.cun.biz.station.bo.impl.CaiNiaoAdapterBOImpl;

public class CaiNiaoAdapterBOImpl implements CaiNiaoAdapterBO {
	
	public static final Logger logger = LoggerFactory.getLogger(CaiNiaoAdapterBOImpl.class);
	public static final String ADDRESS_SPLIT = "^^^";
  
//	@Resource
//	private CountyDomainWriteService countyDomainWriteService;
//	@Resource
//	private StationWriteService stationWriteService;
//	@Resource
//	private StationUserWriteService stationUserWriteService;
//	@Resource
//	private Emp360BO emp360BO;

	@Override
	public Long addCounty(CaiNiaoStationDto station) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long addStation(CaiNiaoStationDto station) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addStationUserRel(CaiNiaoStationDto station, String userType) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyStation(CaiNiaoStationDto station) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateStationUserRel(CaiNiaoStationDto station) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeStationById(Long cainiaoStationId, Long userId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeStationUserRel(Long userId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateStationFeatures(Long stationId, LinkedHashMap<String, String> features)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateStationUserRelFeature(Long userId, Map<String, String> featureMap)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return false;
	}

}
