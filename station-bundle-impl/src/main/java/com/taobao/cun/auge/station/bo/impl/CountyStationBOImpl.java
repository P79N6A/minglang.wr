package com.taobao.cun.auge.station.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.mapper.CountyStationMapper;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

@Component("countyStationBO")
public class CountyStationBOImpl implements CountyStationBO {
	
	@Autowired
	CountyStationMapper countyStationMapper;
	
	@Override
	public CountyStation getCountyStationByOrgId(Long orgId)
			throws AugeServiceException {
		if (orgId == null || orgId < 0) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		CountyStation condition = new CountyStation();
		condition.setOrgId(orgId);
		condition.setIsDeleted("n");
		return countyStationMapper.selectOne(condition);
	}

}
