package com.taobao.cun.auge.qualification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.qualification.service.CuntaoQualificationService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestCuntaoQualificationService {
	
	@Autowired
	private CuntaoQualificationService cuntaoQualificationService;
	
	@Test
	public void testInvalidQualification(){
		cuntaoQualificationService.invalidQualification(3645044160l);
	}
	
	@Test
	public void testRecoverQualification(){
		cuntaoQualificationService.recoverQualification(3645044160l);
	}
}
