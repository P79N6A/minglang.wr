package com.taobao.cun.auge.store;

import java.util.List;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.dto.StoreQueryPageCondition;
import com.taobao.cun.auge.store.service.StoreReadService;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.dto.OrgAddDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestStoreReadService {

	@Autowired
	private StoreReadService storeReadService;
	
	@Autowired
	@Qualifier("storeEndorApiClient")
	private EndorApiClient storeEndorApiClient;
	
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
	
	@Test
	public void test(){
		{
			OrgAddDto orgAddDto = new OrgAddDto();
			orgAddDto.setOrgId(3733394l);
			orgAddDto.setParentId(3L);
			orgAddDto.setOrgName("测试家电门店");
			orgAddDto.setCreator("62333");
			storeEndorApiClient.getOrgServiceClient().insert(orgAddDto, null);
		}
		
		{
			OrgAddDto orgAddDto = new OrgAddDto();
			orgAddDto.setOrgId(3732043l);
			orgAddDto.setParentId(3L);
			orgAddDto.setOrgName("天猫优品便利店");
			orgAddDto.setCreator("62333");
			storeEndorApiClient.getOrgServiceClient().insert(orgAddDto, null);
		}
		
		{
			OrgAddDto orgAddDto = new OrgAddDto();
			orgAddDto.setOrgId(3732429l);
			orgAddDto.setParentId(3L);
			orgAddDto.setOrgName("村淘测试门店4");
			orgAddDto.setCreator("62333");
			storeEndorApiClient.getOrgServiceClient().insert(orgAddDto, null);
		}
	}
}
