package com.taobao.cun.auge.alilang.jingwei;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

public abstract class JingweiTask implements InitializingBean{
	@Value("${notify.alilang.topic}")
	protected String topic;
	@Value("${notify.alilang.messageType}")
	protected String messageType;
	@Value("${alilang.orgId}")
	protected long alilangOrgId;
	
	public abstract void start();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		start();
	}
}
