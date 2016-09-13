package com.taobao.cun.auge.station.service.internal.flows;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.taobao.cun.auge.validator.ValidationHelper;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("testValidationServiceImpl")
@HSFProvider(serviceInterface = TestValidationService.class)
@Validated
public class TestValidationServiceImpl implements TestValidationService {

	@Override
	public String testJsrValidation(@NotEmpty String test) {
		return test;
	}

	@Override
	public String testSpelValidation(String test) {
		ValidationHelper.getInstance(test).reject("test.error.code","#root.equals('test')");
		System.out.println(test);
		return test;
	}

	@Override
	public String testPredictValidation(String test) {
		ValidationHelper.getInstance(test).reject("test.error.code",t -> "test".equals(test));
		System.out.println(test);
		return test;
	}



}
