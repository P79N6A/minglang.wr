package com.taobao.cun.auge.company;

import java.util.List;

import com.beust.jcommander.internal.Lists;
import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;
import com.taobao.cun.auge.company.dto.VendorQueryPageCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestVendorReadService {

	@Autowired
	private VendorReadService vendorReadService;
	
	@Test
	public void testQueryVendorByID(){
		Result<CuntaoServiceVendorDto> result = vendorReadService.queryVendorByID(2l);
		System.out.println(result.getModule());
	}
	
	
	@Test
	public void testQueryVendorByIDS(){
		List list = Lists.newArrayList();
		list.add(2l);
		Result<List<CuntaoServiceVendorDto>> result = vendorReadService.queryVendorByIDS(list);
		System.out.println(result.getModule());
	}
	
	@Test
	public void testQueryVendorByPage(){
		VendorQueryPageCondition companyQueryPageCondition = new VendorQueryPageCondition();
		Result<PageDto<CuntaoServiceVendorDto>> result = vendorReadService.queryVendorByPage(companyQueryPageCondition);
		System.out.println(result.getModule());
	}
}
