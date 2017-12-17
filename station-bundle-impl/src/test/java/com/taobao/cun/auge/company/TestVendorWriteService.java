package com.taobao.cun.auge.company;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;
import com.taobao.cun.auge.company.dto.CuntaoVendorType;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.dto.OrgAddDto;
import com.taobao.cun.endor.base.dto.UserAddDto;
import com.taobao.cun.endor.base.dto.UserRoleAddDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestVendorWriteService {

	@Autowired
	private VendorWriteService companyWriteService;
	
	@Autowired
	@Qualifier("storeEndorApiClient")
	private EndorApiClient storeEndorApiClient;
	
	@Test
	public void testAddVendor(){
		CuntaoServiceVendorDto cuntaoCompanyDto = new CuntaoServiceVendorDto();
		cuntaoCompanyDto.setCompanyName("testCompany1111");
		cuntaoCompanyDto.setMobile("11321123112");
		cuntaoCompanyDto.setOperator("62333");
		cuntaoCompanyDto.setOperatorType(OperatorTypeEnum.BUC);
		cuntaoCompanyDto.setRemark("test123123");
		cuntaoCompanyDto.setTaobaoNick("esverdor1");
		cuntaoCompanyDto.setType(CuntaoVendorType.SERVICE_VENDOR);
	    Result<Long> result = companyWriteService.addVendor(cuntaoCompanyDto);
	    System.out.println(result.toString());
	}
	
	@Test
	public void testAddEndorUser(){
		OrgAddDto orgAddDto = new OrgAddDto();
		orgAddDto.setCreator("62333");
		orgAddDto.setOrgId(50000130l);
		orgAddDto.setOrgName("testCompany1111");
		orgAddDto.setParentId(5l);
		storeEndorApiClient.getOrgServiceClient().insert(orgAddDto, null);
		
		UserAddDto userAddDto = new UserAddDto();
		userAddDto.setCreator("62333");
		userAddDto.setUserId(3728411498l+"");
		userAddDto.setUserName("38a240c781");
		storeEndorApiClient.getUserServiceClient().addUser(userAddDto);
		
		UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
		userRoleAddDto.setCreator("62333");
		userRoleAddDto.setOrgId(50000130l);
		userRoleAddDto.setRoleName(CuntaoEmployeeIdentifier.VENDOR_MANAGER.name());
		userRoleAddDto.setUserId(3728411498l+"");
		storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
	}
	
	@Test
	public void testUpdateVendor(){
		CuntaoServiceVendorDto cuntaoCompanyDto = new CuntaoServiceVendorDto();
		cuntaoCompanyDto.setId(2l);
		cuntaoCompanyDto.setCompanyName("testCompany111122222");
		cuntaoCompanyDto.setMobile("11321123112");
		cuntaoCompanyDto.setOperator("62333");
		cuntaoCompanyDto.setOperatorType(OperatorTypeEnum.BUC);
		cuntaoCompanyDto.setRemark("test1231232333");
		cuntaoCompanyDto.setTaobaoNick("esverdor1");
		cuntaoCompanyDto.setType(CuntaoVendorType.SERVICE_VENDOR);
	    Result<Boolean> result = companyWriteService.updateVendor(cuntaoCompanyDto);
	    System.out.println(result.toString());
	}
}
