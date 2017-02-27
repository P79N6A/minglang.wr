package com.taobao.cun.auge.common;

import java.util.List;

/**
 * 
 * @author zhenhuan.zhangzh
 *
 */
public interface TestUserConfig {

	public List<String> getTestUserTypes();
	
	public List<Long> getTestTaobaoUserIds();
	
	public List<Long> getTestOrgIds();
}
