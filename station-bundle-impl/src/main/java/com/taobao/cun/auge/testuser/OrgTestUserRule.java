package com.taobao.cun.auge.testuser;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;

@Component("testOrgIds")
public class OrgTestUserRule implements  TestUserRule{

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
	
	@Override
	public boolean checkTestUser(Long taobaoUserId, String config) {
		Assert.notNull(taobaoUserId);
		PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		Assert.notNull(partnerInstance);
		Station station = stationBO.getStationById(partnerInstance.getStationId());
		Assert.notNull(partnerInstance);
		Assert.notNull(station);
		String orgIds = config;
		List<Long> testOrgIds = Lists.newArrayList(orgIds.split(",")).stream().map(orgId -> Long.parseLong(orgId)).collect(Collectors.toList());
		return checkTestUser(station.getApplyOrg(),testOrgIds);
	}

	
	private boolean checkTestUser(Long userOrgId,List<Long> testUserOrgIds){
		CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getCuntaoOrg(userOrgId);
		if(testUserOrgIds.contains(cuntaoOrgDto.getId())){
			return true;
		}else{
			if(cuntaoOrgDto.getParent()!=null){
				return checkTestUser(cuntaoOrgDto.getParentId(),testUserOrgIds);
			}
			return false;
		}
	}



	

}
