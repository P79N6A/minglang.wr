package com.taobao.cun.auge.testuser;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;

@Component("testUserType")
public class UserTypeTestUserRule  implements  TestUserRule{

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Override
	public boolean checkTestUser(Long taobaoUserId,String config) {
		String[] userTypes = StringUtils.commaDelimitedListToStringArray(config);
		PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		Assert.notNull(partnerInstance);
		return Stream.of(userTypes).filter(value -> !StringUtils.isEmpty(value)).anyMatch(value -> partnerInstance.getType().equals(value));
	}


}
