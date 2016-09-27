package com.taobao.cun.auge.alilang.jingwei;

import org.springframework.beans.factory.InitializingBean;

public abstract class JingweiTask implements InitializingBean{
	
	public abstract void start();

	@Override
	public void afterPropertiesSet() throws Exception {
		start();
	}
}
