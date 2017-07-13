package com.taobao.cun.auge.failure;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.exception.AugeBusinessException;

@Component
@RefreshScope
public class AugeBussinessExceptionFaulureAnalyzer extends AbstractFailureAnalyzer<AugeBusinessException> {

	@Value("#{'${ignoreBusinessErrorCodes}'.split(',')}")
	private List<String> ignoreBusinessErrorCodes;
	
	
	public boolean isFatal(String errorCode){
		if(errorCode == null || ignoreBusinessErrorCodes == null || ignoreBusinessErrorCodes.isEmpty()){
			return true;
		}
		return !ignoreBusinessErrorCodes.contains(errorCode);
	}
	
	
	@Override
	protected FailureAnalysis analyze(Throwable rootFailure, AugeBusinessException cause) {
		if(cause != null){
			String errorCode = cause.getExceptionCode();
			String action = isFatal(errorCode)?"error":"warn";
			FailureAnalysis failureAnalysis = new FailureAnalysis(cause.getMessage(),action,cause);
			return failureAnalysis;
		}
		return null;
	}

}
