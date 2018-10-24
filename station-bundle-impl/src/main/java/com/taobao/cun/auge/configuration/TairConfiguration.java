package com.taobao.cun.auge.configuration;

import com.taobao.cun.auge.lock.ManualReleaseDistributeLock;
import com.taobao.cun.auge.lock.TairManualReleaseDistributeLock;
import com.taobao.cun.crius.tair.RdbTairFacade;
import com.taobao.cun.crius.tair.facade.LdbTairFacadeImpl;
import com.taobao.cun.crius.tair.facade.MdbTairFacadeImpl;
import com.taobao.cun.crius.tair.facade.RdbTairFacadeImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TairConfiguration {

	@Bean
	public MdbTairFacadeImpl mdbTairFacade() {
		return new MdbTairFacadeImpl();
	}

	@Bean
	public RdbTairFacadeImpl rdbTairFacade() throws Exception {
		return new RdbTairFacadeImpl();
	}

	@Bean
	public LdbTairFacadeImpl ldbTairFacade() {
		return new LdbTairFacadeImpl();
	}

	@Bean
	public ManualReleaseDistributeLock manualReleaseDistributeLock(RdbTairFacade rdbTairFacade) throws Exception {
		TairManualReleaseDistributeLock distributeLock = new TairManualReleaseDistributeLock();
		distributeLock.setRdbTairFacade(rdbTairFacade);
		return distributeLock;
	}

}
