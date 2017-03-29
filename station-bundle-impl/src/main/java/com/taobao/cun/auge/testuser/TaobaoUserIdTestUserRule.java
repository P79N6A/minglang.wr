package com.taobao.cun.auge.testuser;

import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("testTaobaoUserIds")
public class TaobaoUserIdTestUserRule implements  TestUserRule{

	@Override
	public boolean checkTestUser(Long taobaoUserId,String config) {
		return Stream.of(StringUtils.commaDelimitedListToStringArray(config)).filter(value -> !StringUtils.isEmpty(value)).map(Long::parseLong).anyMatch(id -> id.equals(taobaoUserId));
	}


}
