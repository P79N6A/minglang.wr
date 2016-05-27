package com.taobao.cun.auge.station.adapter.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.cainiao.cuntaonetwork.dto.foundation.FeatureDTO;
import com.alibaba.cainiao.cuntaonetwork.param.Modifier;
import com.alibaba.cainiao.cuntaonetwork.param.station.AddStationParam;
import com.alibaba.cainiao.cuntaonetwork.param.station.AddStationUserRelParam;
import com.alibaba.cainiao.cuntaonetwork.param.station.UpdateStationParam;
import com.alibaba.cainiao.cuntaonetwork.param.station.UpdateStationUserRelParam;
import com.alibaba.cainiao.cuntaonetwork.param.warehouse.AddCountyDomainParam;
import com.alibaba.cainiao.cuntaonetwork.result.Result;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationUserWriteService;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationWriteService;
import com.alibaba.cainiao.cuntaonetwork.service.warehouse.CountyDomainWriteService;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.PositionUtil;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.dto.CaiNiaoStationDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public class CaiNiaoAdapterImpl implements CaiNiaoAdapter {
	
	public static final Logger logger = LoggerFactory.getLogger(CaiNiaoAdapterImpl.class);
	public static final String ADDRESS_SPLIT = "^^^";
  
	@Resource
	private CountyDomainWriteService countyDomainWriteService;
	@Resource
	private StationWriteService stationWriteService;
	@Resource
	private StationUserWriteService stationUserWriteService;
	@Resource
	private Emp360Adapter emp360Adapter;


	@Override
	public Long addCounty(CaiNiaoStationDto station) throws AugeServiceException {
		if (station == null) {
			throw new AugeServiceException("CaiNiaoAdapterBO.addCounty.param.error:station is null!");
		}
		try {
			logger.info("addCounty.info param"+JSONObject.toJSONString(station));
			AddCountyDomainParam county = buildAddCountyDomainParam(station);
			Result<Long> res= countyDomainWriteService.addCountyDomain(county,Modifier.newSystem());
			if (!res.isSuccess()) {
				throw new AugeServiceException(res.getErrorCode()+"|"+res.getErrorMessage());
			}
			return res.getData();
		} catch (Exception e) {
			String error = getErrorMessage("addCounty", JSONObject.toJSONString(station),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(error);
		}
	}

	private AddCountyDomainParam buildAddCountyDomainParam(CaiNiaoStationDto dto) {
		AddCountyDomainParam county = new AddCountyDomainParam();
		county.setContact(dto.getContact());
		county.setMobile(dto.getMobile());
		county.setName(dto.getStationName());
		county.setTelephone(dto.getTelephone());
		
		Address cainaioStationAddress = dto.getStationAddress();
		if (cainaioStationAddress != null) {
			if (StringUtil.isNotEmpty(cainaioStationAddress.getProvince())){
				county.setProvinceId(Long.parseLong(cainaioStationAddress.getProvince()));
			}
			
			if (StringUtil.isNotEmpty(cainaioStationAddress.getCity())){
				county.setCityId(Long.parseLong(cainaioStationAddress.getCity()));
			}
			if (StringUtil.isNotEmpty(cainaioStationAddress.getCounty())){
				county.setCountyId(Long.parseLong(cainaioStationAddress.getCounty()));
			}
			if (StringUtil.isNotEmpty(cainaioStationAddress.getTown())){
				county.setTownId(Long.parseLong(cainaioStationAddress.getTown()));
			}
			county.setLat(StringUtils.isEmpty(cainaioStationAddress.getLat())? "0":PositionUtil.converDown(cainaioStationAddress.getLat()));
			county.setLng(StringUtils.isEmpty(cainaioStationAddress.getLng())? "0":PositionUtil.converDown(cainaioStationAddress.getLng()));
			county.setAddress(getAddress(cainaioStationAddress));
		}
		// 县运营中心 同步多个物流人账号,县运营中心，物流操作人，更新，采用邮件方式通知菜鸟，不再通过API方式，因此loginId可能为null
		//county.setWangwangs(getWangWangSets(dto.getLoginId()));
		return county;
	}
	
	private  String getAddress(Address stationAddress) {
		 StringBuilder address = new StringBuilder();
		 if (stationAddress != null) {
			  address.append(stationAddress.getProvinceDetail()).append(ADDRESS_SPLIT)
              .append(StringUtil.isBlank(stationAddress.getCityDetail()) ? " " : stationAddress.getCityDetail())
              .append(ADDRESS_SPLIT)
              .append(StringUtil.isBlank(stationAddress.getCountyDetail()) ? " " : stationAddress.getCountyDetail())
              .append(ADDRESS_SPLIT)
              .append(StringUtil.isBlank(stationAddress.getTownDetail()) ? " " : stationAddress.getTownDetail())
              .append(ADDRESS_SPLIT)
              .append(StringUtil.isBlank(stationAddress.getAddressDetail()) ? " " : stationAddress.getAddressDetail());
		 }
         return address.toString();
	}

	@Override
	public Long addStation(CaiNiaoStationDto station) throws AugeServiceException {
		if (station == null) {
			throw new AugeServiceException("CaiNiaoAdapterBO.addStation.param.error:station is null!");
		}
		try {
			logger.info("addStation.info param"+JSONObject.toJSONString(station));
			AddStationParam addStationParam = buildAddStationParam(station);
			Result<Long> res = stationWriteService.addStation(addStationParam,station.getParentId(), station.getTaobaoUserId(),Modifier.newSystem());
			if (!res.isSuccess()) {
				throw new AugeServiceException(res.getErrorCode()+"|"+res.getErrorMessage());
			}
			// 设置菜鸟站点id;
			station.setStationId(res.getData());
			boolean userRel = this.addStationUserRel(station,CaiNiaoStationDto.USERTYPE_TP);
			if (!userRel) {
				throw new AugeServiceException("addStationUserRel.res is false");
			}
			return res.getData();
		} catch (Exception e) {
			String error = getErrorMessage("addStation", JSONObject.toJSONString(station),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(error);
		}
	}

	private AddStationUserRelParam bulidAddStationUserRelParam(CaiNiaoStationDto dto,
			String userType) {
		AddStationUserRelParam param = new AddStationUserRelParam();
		FeatureDTO feature = new FeatureDTO();
		if (CaiNiaoStationDto.USERTYPE_TPA.equals(userType)) {
			feature.put(CaiNiaoAdapter.PARTNER_ID, String.valueOf(dto.getTpTaobaoUserId()));
		} else if (CaiNiaoStationDto.USERTYPE_TP.equals(userType)) {
			feature.put(CaiNiaoAdapter.PARTNER_ID, String.valueOf(dto.getTaobaoUserId()));
		}
		param.setFeature(feature);
		param.setReceiverMobile(dto.getMobile());
		param.setReceiverName(dto.getContact());
		param.setReceiverPhone(dto.getTelephone());
		// param.setReceiverZipcode(receiverZipcode);
		param.setStationId(dto.getStationId());
		param.setUserId(dto.getTaobaoUserId());
		return param;
	}

	private AddStationParam buildAddStationParam(CaiNiaoStationDto dto) {
		AddStationParam cnStation = new AddStationParam();
		// cnStation.setAlipayAccount(dto.getAlipayAccount()); 历史没传
		// cnStation.setEmail(email); 历史没传
		cnStation.setContact(dto.getContact());
		cnStation.setCtCode(dto.getStationNum());
		FeatureDTO feature = new FeatureDTO();
		feature.put(CaiNiaoAdapter.CUNTAO_CODE, dto.getStationNum());
		cnStation.setFeature(feature);
		// cnStation.setId(id);
		cnStation.setMobile(dto.getMobile());
		cnStation.setName(dto.getStationName());
		cnStation.setPhone(dto.getTelephone());
		cnStation.setWangwang(dto.getLoginId());
		// cnStation.setZipCode(zipCode);
		
		Address stationAddress = dto.getStationAddress();
		if (stationAddress != null) {
			if (StringUtil.isNotEmpty(stationAddress.getProvince())){
				cnStation.setProvinceId(Long.parseLong(stationAddress.getProvince()));
			}
			
			if (StringUtil.isNotEmpty(stationAddress.getCity())){
				cnStation.setCityId(Long.parseLong(stationAddress.getCity()));
			}
			if (StringUtil.isNotEmpty(stationAddress.getCounty())){
				cnStation.setCountyId(Long.parseLong(stationAddress.getCounty()));
			}
			if (StringUtil.isNotEmpty(stationAddress.getTown())){
				cnStation.setTownId(Long.parseLong(stationAddress.getTown()));
			}
			
			cnStation.setLat(StringUtil.isEmpty(stationAddress.getLat()) ? "0" : PositionUtil.converDown(stationAddress.getLat()));
			cnStation.setLng(StringUtil.isEmpty(stationAddress.getLng()) ? "0" : PositionUtil.converDown(stationAddress.getLng()));
			//cnStation.setAreaCode(addressMap.get("countyId").toString());
			if (stationAddress != null && StringUtil.isNotEmpty(stationAddress.getVillage())) {
				cnStation.setCountryId(Long.parseLong(stationAddress.getVillage()));
			}
			cnStation.setAddress(getAddress(stationAddress));
		}
		cnStation.setStationOpenTime(new Date());
		return cnStation;
	}

	@Override
	public boolean addStationUserRel(CaiNiaoStationDto station, String userType)
			throws AugeServiceException {
		if (station == null) {
			throw new AugeServiceException(getErrorMessage("addStationUserRel", "","station is null!"));
		}
		if (userType == null) {
			throw new AugeServiceException(getErrorMessage("addStationUserRel", "","userType is null!"));
		}
		try {
			logger.info("addStationUserRel.info param"+JSONObject.toJSONString(station));
			AddStationUserRelParam relParam = bulidAddStationUserRelParam(
					station, userType);
			Result<Boolean> res = stationUserWriteService.addStationUserRel(relParam, Modifier.newSystem());
			if (!res.isSuccess()) {
				throw new AugeServiceException(res.getErrorCode()+"|"+res.getErrorMessage());
			} 
			return res.getData();
		} catch (Exception e) {
			String error = getErrorMessage("addStationUserRel", JSONObject.toJSONString(station)+",userType:"+userType,e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(error);
		}
	}

	@Override
	public boolean modifyStation(CaiNiaoStationDto station) throws AugeServiceException {
		if (station == null) {
			throw new AugeServiceException(getErrorMessage("modifyStation", "","station is null"));
		}
		if (station.getTaobaoUserId() == null) {
			throw new AugeServiceException(getErrorMessage("modifyStation", "","taobaoUserId is null"));
		}
		try {
			logger.info("modifyStation.info param"+JSONObject.toJSONString(station));
			UpdateStationParam updateStationParam = buildUpdateStationParam(station);
			Result<Boolean> res = stationWriteService.updateStation(
					updateStationParam, Modifier.newSystem());
			if (!res.isSuccess()) {
				throw new AugeServiceException(res.getErrorCode()+"|"+res.getErrorMessage());
			}
			if (!res.getData()) {
				throw new AugeServiceException("updateStation is false");
			}
			boolean userRel = this.updateStationUserRel(station);
			if (!userRel) {
				throw new AugeServiceException("updateStationUserRel.res is false");
			}
			LinkedHashMap<String, String> featureMap = new LinkedHashMap<String, String>();
			featureMap.put(CaiNiaoAdapter.CUNTAO_CODE, station.getStationNum());
			this.updateStationFeatures(station.getStationId(), featureMap);
			return true;
		} catch (Exception e) {
			String error = getErrorMessage("modifyStation", JSONObject.toJSONString(station),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(error);
		}
	}

	private UpdateStationParam buildUpdateStationParam(CaiNiaoStationDto dto) {
		UpdateStationParam cnStation = new UpdateStationParam();
		// cnStation.setAlipayAccount(dto.getAlipayAccount()); 历史没传
		// cnStation.setEmail(email); 历史没传
		cnStation.setContact(dto.getContact());
		cnStation.setCtCode(dto.getStationNum());
		//FeatureDTO feature = new FeatureDTO();
		//feature.add(CUNTAO_CODE, dto.getStationNum());
		//cnStation.setFeature(feature);
		cnStation.setId(dto.getStationId());
		cnStation.setMobile(dto.getMobile());
		cnStation.setName(dto.getStationName());
		cnStation.setPhone(dto.getTelephone());
		cnStation.setWangwang(dto.getLoginId());
		// cnStation.setZipCode(zipCode); 没有
		Address stationAddress = dto.getStationAddress();
		if (stationAddress != null) {
			if (StringUtil.isNotEmpty(stationAddress.getProvince())){
				cnStation.setProvinceId(Long.parseLong(stationAddress.getProvince()));
			}
			
			if (StringUtil.isNotEmpty(stationAddress.getCity())){
				cnStation.setCityId(Long.parseLong(stationAddress.getCity()));
			}
			if (StringUtil.isNotEmpty(stationAddress.getCounty())){
				cnStation.setCountyId(Long.parseLong(stationAddress.getCounty()));
			}
			if (StringUtil.isNotEmpty(stationAddress.getTown())){
				cnStation.setTownId(Long.parseLong(stationAddress.getTown()));
			}
			
			cnStation.setLat(StringUtil.isEmpty(stationAddress.getLat()) ? "0" : PositionUtil.converDown(stationAddress.getLat()));
			cnStation.setLng(StringUtil.isEmpty(stationAddress.getLng()) ? "0" : PositionUtil.converDown(stationAddress.getLng()));
			//cnStation.setAreaCode(addressMap.get("countyId"));
			if (dto.getStationAddress() != null && StringUtil.isNotEmpty(dto.getStationAddress().getVillage())) {
				cnStation.setCountryId(Long.parseLong(dto.getStationAddress().getVillage()));
			}
			cnStation.setAddress(getAddress(dto.getStationAddress()));
		}
		return cnStation;
	}

	private UpdateStationUserRelParam bulidUpdateStationUserRelParam(
			CaiNiaoStationDto dto) {
		UpdateStationUserRelParam param = new UpdateStationUserRelParam();
		/*FeatureDTO feature = new FeatureDTO();
		if (StationDto.USERTYPE_TPA.equals(userType)) {
			feature.add("partnerId", String.valueOf(dto.getTpTaobaoUserId()));
		} else if (StationDto.USERTYPE_TP.equals(userType)) {
			feature.add("partnerId", String.valueOf(dto.getTaobaoUserId()));
		}
		param.setFeature(feature);*/
		param.setReceiverMobile(dto.getMobile());
		param.setReceiverName(dto.getContact());
		param.setReceiverPhone(dto.getTelephone());
		// param.setReceiverZipcode(receiverZipcode); 没有
		param.setStationId(dto.getStationId());
		param.setUserId(dto.getTaobaoUserId());
		return param;
	}

	@Override
	public boolean updateStationUserRel(CaiNiaoStationDto station)
			throws AugeServiceException {
		if (station == null) {
			throw new AugeServiceException("CaiNiaoAdapterBO.updateStationUserRel.param.error:station is null!");
		}
		try {
			logger.info("updateStationUserRel.info param"+JSONObject.toJSONString(station));
			UpdateStationUserRelParam updateParam = bulidUpdateStationUserRelParam(station);
			Result<Boolean> res = stationUserWriteService.updateStationUserRel(updateParam, Modifier.newSystem());
			if (!res.isSuccess()) {
				throw new AugeServiceException(res.getErrorCode()+"|"+res.getErrorMessage());
			}
			return res.getData();
	    } catch (Exception e) {
			String error = getErrorMessage("updateStationUserRel", JSONObject.toJSONString(station),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(error);
	    }
	}

	@Override
	public boolean removeStationById(Long cainiaoStationId, Long userId) throws AugeServiceException {
		if (cainiaoStationId == null) {
			throw new AugeServiceException("CaiNiaoAdapterBO.removeStationById.param.error:cainiaoStationId is null!");
		}
		if (userId == null) {
			throw new AugeServiceException("CaiNiaoAdapterBO.removeStationById.param.error:userId is null!");
		}
		try {
			logger.info("removeStationById.info cainiaoStationId"+cainiaoStationId+"userId"+userId);
			boolean relResult = removeStationUserRel(userId);
			if (!relResult) {
				throw new AugeServiceException("removeStationUserRel.res is false");
			}
			Result<Boolean> res = stationWriteService.removeStationById(cainiaoStationId,Modifier.newSystem());
			if (!res.isSuccess()) {
				throw new AugeServiceException(res.getErrorCode()+"|"+res.getErrorMessage());
			}
			return res.getData();
		} catch (Exception e) {
			String error = getErrorMessage("removeStationById", cainiaoStationId.toString(),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(error);
	    }
	}
	
	private String getErrorMessage(String methodName,String param,String error) {
		StringBuilder sb = new StringBuilder();
		sb.append("CaiNiaoAdapterBO-Error|").append(methodName).append("(.param=").append(param).append(").").append("errorMessage:").append(error);
		return sb.toString();
	}

	@Override
	public boolean removeStationUserRel(Long userId) throws AugeServiceException {
		if (userId == null) {
			throw new AugeServiceException(
					"CaiNiaoAdapterBO.removeStationUserRel.param.error:userId is null!");
		}
		try {
			logger.info("removeStationById.info userId"+userId);
			Result<Boolean> res = stationUserWriteService.removeStationUserRel(userId, Modifier.newSystem());
			if (!res.isSuccess()) {
				throw new AugeServiceException(res.getErrorCode()+"|"+res.getErrorMessage());
			} 
			return res.getData();
		} catch (Exception e) {
			String error = getErrorMessage("removeStationUserRel", userId.toString(),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(error);
	    }
	}

	@Override
	public boolean updateStationFeatures(Long stationId,
			LinkedHashMap<String, String> features) throws AugeServiceException {
		if (stationId == null) {
			throw new AugeServiceException("CaiNiaoAdapterBO.updateStationFeature.param.error:stationId is null!");
		}
		if (features == null || features.size()==0) {
			throw new AugeServiceException("CaiNiaoAdapterBO.updateStationFeature.param.features is null!");
		}
		try {
			logger.info("updateStationFeatures.info stationId"+stationId+"featureMap"+features);
			Result<Boolean> res = stationWriteService.putStationFeatures(stationId, features, Modifier.newSystem());
			if (!res.isSuccess()) {
				throw new AugeServiceException(res.getErrorCode()+"|"+res.getErrorMessage());
			} 
			return res.getData();
		} catch (Exception e) {
			String error = getErrorMessage("updateStationFeature", stationId.toString()+" featureMap:"+features.toString(),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(error);
	    }
	}
	

	@Override
	public boolean updateStationUserRelFeature(Long userId,
			Map<String, String> featureMap) throws AugeServiceException {
		if (userId == null) {
			throw new AugeServiceException("CaiNiaoAdapterBO.updateStationUserRelFeature.param.error:userId is null!");
		}
		if (featureMap == null || featureMap.size()==0) {
			throw new AugeServiceException("CaiNiaoAdapterBO.updateStationUserRelFeature.param.featureMap is null!");
		}
		try {
			logger.info("updateStationUserRelFeature.info userId"+userId+"featureMap"+featureMap);
			Result<Boolean> res = stationUserWriteService.putStationUserRelFeature(userId, featureMap,Modifier.newSystem());
			if (!res.isSuccess()) {
				throw new AugeServiceException(res.getErrorCode()+"|"+res.getErrorMessage());
			} 
			return res.getData();
		} catch (Exception e) {
			String error = getErrorMessage("updateStationUserRelFeature", userId.toString()+" featureMap:"+featureMap.toString(),e.getMessage());
			logger.error(error,e);
			throw new AugeServiceException(error);
	    }
	}
}
