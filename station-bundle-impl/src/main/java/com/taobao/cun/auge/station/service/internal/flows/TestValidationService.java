package com.taobao.cun.auge.station.service.internal.flows;

import org.hibernate.validator.constraints.NotEmpty;

public interface TestValidationService {

	public String testJsrValidation(@NotEmpty String test);
	
	public String testSpelValidation(String test);
	
	public String testPredictValidation(String test);
}
