package com.taobao.cun.auge.company;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.CuntaoVendorEmployeeType;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.endor.base.client.EndorApiClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestEmployeeWriteService {

	@Autowired
	private EmployeeWriteService employeeWriteService;
	
	@Autowired
	private EndorApiClient endorApiClient;
	@Test
	public void testAddEmployee(){
		CuntaoEmployeeDto employee = new CuntaoEmployeeDto();
		employee.setTaobaoNick("fangyutest013");
		employee.setName("测试配送员");
		employee.setMobile("12312312312");
		employee.setOperator("62333");
		employee.setOperatorType(OperatorTypeEnum.BUC);
		Result<Long>  result = employeeWriteService.addEmployee(2l, employee, CuntaoVendorEmployeeType.DISTRIBUTOR);
		Assert.notNull(result.getModule());
	}
	
	
	@Test
	public void testUpdateEmployee(){
		CuntaoEmployeeDto employee = new CuntaoEmployeeDto();
		employee.setId(4l);
		employee.setName("测试配送员2");
		employee.setMobile("31231223312");
		employee.setOperator("62333");
		employee.setOperatorType(OperatorTypeEnum.BUC);
		Result<Boolean>  result = employeeWriteService.updateEmployee(employee);
		Assert.notNull(result.getModule());
	}
	
	
	
	@Test
	public void testPermission(){
		boolean result = endorApiClient.getUserPermissionServiceClient().check("3728411498", "servicevendor_manager");
		Assert.isTrue(result);
	}
}
