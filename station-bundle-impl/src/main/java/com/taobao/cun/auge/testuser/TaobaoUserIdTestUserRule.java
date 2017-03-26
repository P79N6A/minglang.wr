package com.taobao.cun.auge.testuser;

import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TaobaoUserIdTestUserRule extends AbstractTestUserRule{

	@Override
	public boolean doCheckTestUser(Long taobaoUserId,String config) {
		return Stream.of(StringUtils.commaDelimitedListToStringArray(config)).map(Long::parseLong).anyMatch(id -> id.equals(taobaoUserId));
	}

	@Override
	public String getConfigKey() {
		return "testTaobaoUserIds";
	}

}
