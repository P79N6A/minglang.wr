package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationExample;
import com.taobao.cun.auge.dal.domain.CountyStationExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CountyStationMapper;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("countyStationBO")
public class CountyStationBOImpl implements CountyStationBO {
	
	@Autowired
	CountyStationMapper countyStationMapper;
	
	@Override
	public CountyStation getCountyStationByOrgId(Long orgId)
			 {
		ValidateUtils.notNull(orgId);
		CountyStationExample example = new CountyStationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andOrgIdEqualTo(orgId);
		List<CountyStation> resList = countyStationMapper.selectByExample(example);
		return ResultUtils.selectOne(resList);
	}

	@Override
	public CountyStation getCountyStationById(Long id) throws AugeServiceException {
		ValidateUtils.notNull(id);
		CountyStationExample example = new CountyStationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andIdEqualTo(id);
		List<CountyStation> resList = countyStationMapper.selectByExample(example);
		return ResultUtils.selectOne(resList);
	}

	@Override
	public Long addCountyStation(CountyStation cs){
		countyStationMapper.insert(cs);
		return cs.getId();
	}
	
	@Override
	public void updateOwnDept(Long id, String ownDept) {
		CountyStation countyStation = countyStationMapper.selectByPrimaryKey(id);
		countyStation.setOwnDept(ownDept);
		countyStation.setGmtModified(new Date());
		countyStationMapper.updateByPrimaryKey(countyStation);
	}
}
