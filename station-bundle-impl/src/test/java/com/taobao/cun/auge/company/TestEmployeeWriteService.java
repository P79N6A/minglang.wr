package com.taobao.cun.auge.company;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.dto.OrgAddDto;
import com.taobao.cun.endor.base.dto.UserAddDto;
import com.taobao.cun.endor.base.dto.UserRoleAddDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestEmployeeWriteService {

	@Autowired
	private EmployeeWriteService employeeWriteService;
	
	@Autowired
	@Qualifier("storeEndorApiClient")
	private EndorApiClient storeEndorApiClient;
	@Test
	public void testAddEmployee(){
		CuntaoEmployeeDto employee = new CuntaoEmployeeDto();
		employee.setTaobaoNick("zhuzi102");
		employee.setName("测试配送员");
		employee.setMobile("12312312310");
		employee.setOperator("62333");
		Result<Long>  result = employeeWriteService.addVendorEmployee(2l, employee, CuntaoEmployeeIdentifier.VENDOR_DISTRIBUTOR);
		Assert.notNull(result.getModule());
	}
	
	
	@Test
	public void testUpdateEmployee(){
		CuntaoEmployeeDto employee = new CuntaoEmployeeDto();
		employee.setId(4l);
		employee.setName("测试配送员2");
		employee.setMobile("31231223312");
		employee.setOperator("62333");
		Result<Boolean>  result = employeeWriteService.updateVendorEmployee(employee);
		Assert.notNull(result.getModule());
	}
	
	@Test
	public void test2(){
		Result<Long>  result = employeeWriteService.addVendorEmployeeByEmployeeId(50000130l, 2l, CuntaoEmployeeIdentifier.VENDOR_DISTRIBUTOR);
		Assert.notNull(result.getModule());
	}
	
	
	@Test
	public void test(){
		
			
		{
			UserAddDto userAddDto = new UserAddDto();
			userAddDto.setCreator("62333");
			userAddDto.setUserId(3728290555l+"");
			userAddDto.setUserName("测试配送员7");
			storeEndorApiClient.getUserServiceClient().addUser(userAddDto);
			
			UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
			userRoleAddDto.setCreator("62333");
			userRoleAddDto.setOrgId(50000130l);
			userRoleAddDto.setRoleName(CuntaoEmployeeIdentifier.VENDOR_DISTRIBUTOR.name());
			userRoleAddDto.setUserId(3728290555l+"");
			storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
		}
			
		{
			UserAddDto userAddDto = new UserAddDto();
			userAddDto.setCreator("62333");
			userAddDto.setUserId(3728290383l+"");
			userAddDto.setUserName("测试配送员8");
			storeEndorApiClient.getUserServiceClient().addUser(userAddDto);
			
			UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
			userRoleAddDto.setCreator("62333");
			userRoleAddDto.setOrgId(50000130l);
			userRoleAddDto.setRoleName(CuntaoEmployeeIdentifier.VENDOR_DISTRIBUTOR.name());
			userRoleAddDto.setUserId(3728290383l+"");
			storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
		}
			
		{
			UserAddDto userAddDto = new UserAddDto();
			userAddDto.setCreator("62333");
			userAddDto.setUserId(3728400409l+"");
			userAddDto.setUserName("配送员");
			storeEndorApiClient.getUserServiceClient().addUser(userAddDto);
			
			UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
			userRoleAddDto.setCreator("62333");
			userRoleAddDto.setOrgId(50000130l);
			userRoleAddDto.setRoleName(CuntaoEmployeeIdentifier.VENDOR_DISTRIBUTOR.name());
			userRoleAddDto.setUserId(3728400409l+"");
			storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
		}
		
	}
	@Test
	public void testAddStoreEmployee(){
		CuntaoEmployeeDto employee = new CuntaoEmployeeDto();
		employee.setTaobaoNick("gyprotocol02");
		employee.setName("测试配货员2");
		employee.setMobile("12312312319");
		employee.setOperator("62333");
		employeeWriteService.addStoreEmployee(3732591l, employee, CuntaoEmployeeIdentifier.STORE_PICKER);
	}
	
	
	@Test
	public void test3(){
		{
			CuntaoEmployeeDto employee = new CuntaoEmployeeDto();
			employee.setTaobaoNick("baozhengjin");
			employee.setName("测试配送员1");
			employee.setMobile("11111111111");
			employee.setOperator("62333");
			Result<Long>  result = employeeWriteService.addVendorEmployee(50000004l, employee, CuntaoEmployeeIdentifier.VENDOR_DISTRIBUTOR);
			Assert.notNull(result.getModule());
		}
		
		{
			CuntaoEmployeeDto employee = new CuntaoEmployeeDto();
			employee.setTaobaoNick("zhanlingtest02");
			employee.setName("测试配送员2");
			employee.setMobile("11111111112");
			employee.setOperator("62333");
			Result<Long>  result = employeeWriteService.addVendorEmployee(50000004l, employee, CuntaoEmployeeIdentifier.VENDOR_DISTRIBUTOR);
			Assert.notNull(result.getModule());
		}
		
		{
			CuntaoEmployeeDto employee = new CuntaoEmployeeDto();
			employee.setTaobaoNick("zhanlingtest01");
			employee.setName("测试配送员3");
			employee.setMobile("11111111113");
			employee.setOperator("62333");
			Result<Long>  result = employeeWriteService.addVendorEmployee(50000004l, employee, CuntaoEmployeeIdentifier.VENDOR_DISTRIBUTOR);
			Assert.notNull(result.getModule());
		}
	}
	
	
	
	
	
	@Test
	public void testPermission(){
		OrgAddDto orgAddDto = new OrgAddDto();
		orgAddDto.setCreator("62333");
		orgAddDto.setOrgId(50000004l);
		orgAddDto.setOrgName("esvendor102服务商");
		orgAddDto.setParentId(5l);
		storeEndorApiClient.getOrgServiceClient().insert(orgAddDto, null);
		
		UserAddDto userAddDto = new UserAddDto();
		userAddDto.setCreator("62333");
		userAddDto.setUserId(3728471526l+"");
		userAddDto.setUserName("14ded4509a");
		storeEndorApiClient.getUserServiceClient().addUser(userAddDto);
		
		UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
		userRoleAddDto.setCreator("62333");
		userRoleAddDto.setOrgId(50000004l);
		userRoleAddDto.setRoleName(CuntaoEmployeeIdentifier.VENDOR_MANAGER.name());
		userRoleAddDto.setUserId(3728471526l+"");
		storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
	}
}
