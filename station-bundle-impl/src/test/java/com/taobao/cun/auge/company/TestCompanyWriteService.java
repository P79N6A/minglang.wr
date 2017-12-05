package com.taobao.cun.auge.company;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;
import com.taobao.cun.auge.company.dto.CuntaoVendorType;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestCompanyWriteService {

	@Autowired
	private VendorWriteService companyWriteService;
	
	@Test
	public void testAddCompany(){
		CuntaoServiceVendorDto cuntaoCompanyDto = new CuntaoServiceVendorDto();
		cuntaoCompanyDto.setCompanyName("testCompany1111");
		cuntaoCompanyDto.setMobile("11321123112");
		cuntaoCompanyDto.setOperator("62333");
		cuntaoCompanyDto.setOperatorType(OperatorTypeEnum.BUC);
		cuntaoCompanyDto.setRemark("test123123");
		cuntaoCompanyDto.setTaobaoNick("fangyutest013");
		cuntaoCompanyDto.setType(CuntaoVendorType.SERVICE_VENDOR);
	    Result<Long> result = companyWriteService.addVendor(cuntaoCompanyDto);
	    System.out.println(result.toString());
	}
}
