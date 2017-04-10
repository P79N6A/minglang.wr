package com.taobao.cun.auge.qualification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class C2BErrorMessageConverter {

	@Autowired
	private ErrorMessageProperties errorMessageProperties;
	
	public String convertErrorMsg(String errorCode, String errorMsg) {
		String errorMsgConverted = errorMessageProperties.getErrorMessage(errorCode);
		if (errorMsgConverted != null) {
			return errorMsgConverted;
		}
		return errorMsg;
	}
	
}
