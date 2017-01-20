package com.taobao.cun.auge.station.bo.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerTypeChangeApply;
import com.taobao.cun.auge.dal.domain.PartnerTypeChangeApplyExample;
import com.taobao.cun.auge.dal.domain.PartnerTypeChangeApplyExample.Criteria;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.PartnerTypeChangeApplyMapper;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerTypeChangeApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerTypeChangeApplyDtoConverter;
import com.taobao.cun.auge.station.dto.PartnerTypeChangeApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.validator.BeanValidator;

@Component("partnerTypeChangeApplyBO")
public class PartnerTypeChangeApplyBOImpl implements PartnerTypeChangeApplyBO {

	@Autowired
	PartnerTypeChangeApplyMapper partnerTypeChangeApplyMapper;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	StationBO stationBO;

	@Override
	public Boolean isUpgradePartnerInstance(Long nextInstanceId,PartnerInstanceTypeChangeEnum typeChangeEnum) throws AugeServiceException, AugeSystemException {
		ValidateUtils.notNull(nextInstanceId);

		PartnerTypeChangeApplyExample example = new PartnerTypeChangeApplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andNextPartnerInstanceIdEqualTo(nextInstanceId);
		criteria.andTypeEqualTo(typeChangeEnum.getType().name());
		int num = partnerTypeChangeApplyMapper.countByExample(example);

		return 0 != num;
	}

	@Override
	public PartnerTypeChangeApplyDto getPartnerTypeChangeApply(Long upgradeInstanceId)	throws AugeServiceException, AugeSystemException {
		ValidateUtils.notNull(upgradeInstanceId);

		PartnerTypeChangeApplyExample example = new PartnerTypeChangeApplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andNextPartnerInstanceIdEqualTo(upgradeInstanceId);

		List<PartnerTypeChangeApply> applies = partnerTypeChangeApplyMapper.selectByExample(example);

		return PartnerTypeChangeApplyDtoConverter.convert(ResultUtils.selectOne(applies));
	}

	@Override
	public Long addPartnerTypeChangeApply(PartnerTypeChangeApplyDto applyDto)	throws AugeServiceException, AugeSystemException {
		BeanValidator.validateWithThrowable(applyDto);
		
		PartnerTypeChangeApply apply = PartnerTypeChangeApplyDtoConverter.convert(applyDto);
		DomainUtils.beforeInsert(apply, applyDto.getOperator());
		
		partnerTypeChangeApplyMapper.insertSelective(apply);
		return apply.getId();
		
	}

	@Override
	public void deletePartnerTypeChangeApply(Long applyId, String operator) throws AugeServiceException, AugeSystemException {
		ValidateUtils.notNull(applyId);

		PartnerTypeChangeApply apply = new PartnerTypeChangeApply();
		apply.setId(applyId);
		DomainUtils.beforeDelete(apply, operator);
		partnerTypeChangeApplyMapper.updateByPrimaryKeySelective(apply);
	}

	@Override
	public StationDto fillStationDto(PartnerTypeChangeApplyDto applyDto)throws AugeServiceException, AugeSystemException {
		Long stationId = partnerInstanceBO.findStationIdByInstanceId(applyDto.getPartnerInstanceId());
		
		Map<String, String> feature = applyDto.getFeature();
		StationDto stationDto= new StationDto();
		
		stationDto.setId(stationId);
		stationDto.setStationNum(feature.get("stationNum"));
		stationDto.setName(feature.get("stationName"));
		
		Address address = new Address();
		address.setProvince(feature.get("province"));
		address.setProvinceDetail(feature.get("provinceDetail"));
		
		address.setCity(feature.get("city"));
		address.setCityDetail(feature.get("cityDetail"));
		
		address.setCounty(feature.get("county"));
		address.setCountyDetail(feature.get("countyDetail"));
		
		address.setTown(feature.get("town"));
		address.setTownDetail(feature.get("townDetail"));
		
		address.setVillage(feature.get("village"));
		address.setVillageDetail(feature.get("villageDetail"));
		
		address.setAddressDetail(feature.get("address"));
		
		address.setLat(feature.get("lat"));
		address.setLng(feature.get("lng"));
		
		stationDto.setAddress(address);
		
		return stationDto;
	}
	
	@Override
	public Map<String, String> backupStationInfo(Long stationId) {
		Station station = stationBO.getStationById(stationId);
		
		Map<String, String> feature = new HashMap<String,String>();
		feature.put("stationNum", station.getStationNum());
		feature.put("stationName", station.getName());
		
		feature.put("province", station.getProvince());
		feature.put("provinceDetail", station.getProvinceDetail());
		feature.put("city", station.getCity());
		feature.put("cityDetail", station.getCityDetail());
		feature.put("county", station.getCounty());
		feature.put("countyDetail", station.getCountyDetail());
		feature.put("town", station.getTown());
		feature.put("townDetail", station.getTownDetail());
		feature.put("village", station.getVillage());
		feature.put("villageDetail", station.getVillageDetail());
		feature.put("address", station.getAddress());
		
		feature.put("lat", station.getLat());
		feature.put("lng", station.getLng());
		return feature;
	}

}
