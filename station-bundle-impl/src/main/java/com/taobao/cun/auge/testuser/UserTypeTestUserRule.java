package com.taobao.cun.auge.testuser;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;

@Component
public class UserTypeTestUserRule extends AbstractTestUserRule{

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Override
	public boolean doCheckTestUser(Long taobaoUserId,String config) {
		String[] userTypes = StringUtils.commaDelimitedListToStringArray(config);
		PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		Assert.notNull(partnerInstance);
		return Stream.of(userTypes).anyMatch(value -> partnerInstance.getType().equals(value));
	}

	@Override
	public String getConfigKey() {
		return "testUserType";
	}

}
