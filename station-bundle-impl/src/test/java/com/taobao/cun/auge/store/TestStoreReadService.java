package com.taobao.cun.auge.store;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreQueryPageCondition;
import com.taobao.cun.auge.store.service.StoreReadService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestStoreReadService {

	@Autowired
	private StoreReadService storeReadService;
	
	@Test
	public void testQueryStoreByPage(){
		StoreQueryPageCondition storeQueryPageCondition = new StoreQueryPageCondition();
		storeQueryPageCondition.setPageNum(1);
		storeQueryPageCondition.setPageSize(10);
		Result<PageDto<StoreDto>>  result = storeReadService.queryStoreByPage(storeQueryPageCondition);
		Assert.notNull(result);
	}
	
	
	@Test
	public void testGetStoreBySharedStoreIds(){
		List<Long> ids = Lists.newArrayList(1002994008l, 1002994007l);
		List<StoreDto> stores = storeReadService.getStoreBySharedStoreIds(ids);
		Assert.notNull(stores);
	}
}
