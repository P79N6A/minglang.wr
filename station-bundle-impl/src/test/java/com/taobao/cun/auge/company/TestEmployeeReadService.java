package com.taobao.cun.auge.company;

import java.util.List;

import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.EmployeeQueryPageCondition;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestEmployeeReadService {

	@Autowired
	private EmployeeReadService employeeReadService;
	
	//@Test
	public void testQueryEmployeeByID(){
		Result<CuntaoEmployeeDto> empoloyee = employeeReadService.queryVendorEmployeeByID(2l);
		Assert.notNull(empoloyee.getModule());
	}
	
	@Test
	public void testQueryEmployeeByIDS(){
		List<Long> ids = Lists.newArrayList();
		ids.add(2L);
		Result<List<CuntaoEmployeeDto>> empoloyees = employeeReadService.queryVendorEmployeeByIDS(ids);
		Assert.notNull(empoloyees.getModule());
	}
	
	
	@Test
	public void testQueryEmployeeByPage(){
		EmployeeQueryPageCondition employeeQueryPageCondition = new EmployeeQueryPageCondition();
		employeeQueryPageCondition.setVendorId(2l);
		Result<PageDto<CuntaoEmployeeDto>> result = employeeReadService.queryVendorEmployeeByPage(employeeQueryPageCondition);
		Assert.notNull(result.getModule());
	}
}
