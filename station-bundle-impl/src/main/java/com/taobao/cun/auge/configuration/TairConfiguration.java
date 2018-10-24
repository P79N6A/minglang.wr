package com.taobao.cun.auge.configuration;

import com.taobao.cun.auge.lock.ManualReleaseDistributeLock;
import com.taobao.cun.auge.lock.TairManualReleaseDistributeLock;
import com.taobao.cun.crius.tair.RdbTairFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TairConfiguration {

	@Bean
	public ManualReleaseDistributeLock manualReleaseDistributeLock(RdbTairFacade rdbTairFacade) throws Exception {
		TairManualReleaseDistributeLock distributeLock = new TairManualReleaseDistributeLock();
		distributeLock.setRdbTairFacade(rdbTairFacade);
		return distributeLock;
	}

}
