package com.taobao.cun.auge.store;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.store.service.StoreWriteService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestStoreWriteService {

	@Autowired
	private StoreWriteService storeWriteService;
	
	@Test
	public void testCreateSampleStore(){
		storeWriteService.createSampleStore(3733394l);
	}
}
