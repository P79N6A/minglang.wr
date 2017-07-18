package com.taobao.cun.auge.failure;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.exception.AugeBusinessException;

public class AugeFailureAnalyzer  implements FailureAnalyzer{

	private AugeFailureConfiguration augeFailureConfiguration;
	
	public AugeFailureAnalyzer(AugeFailureConfiguration augeFailureConfiguration){
		this.augeFailureConfiguration = augeFailureConfiguration;
	}
	
	public boolean isFatal(Throwable failure){
		AugeBusinessException exception = findCause(failure,AugeBusinessException.class);
		if(exception != null){
			List<String> ignoreBusinessErrorCodes = augeFailureConfiguration.getIgnoreBusinessErrorCodes().stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
			if(exception.getExceptionCode() == null || ignoreBusinessErrorCodes == null || ignoreBusinessErrorCodes.isEmpty()){
				return true;
			}
			return !ignoreBusinessErrorCodes.contains(exception.getExceptionCode());
		}
		return true;
	}
	
	public boolean isBusinessException(Throwable failure){
		return findCause(failure,AugeBusinessException.class)!=null;
	}
	
	protected final <T> T  findCause(Throwable failure, Class<T> type) {
		while (failure != null) {
			if (type.isInstance(failure)) {
				return (T) failure;
			}
			failure = failure.getCause();
		}
		return null;
	}
	


	@Override
	public FailureAnalysis analyze(Throwable failure) {
		return null;
	}


	public FailureAnalysis analyze(Throwable failure,String invokeParameters,String bizType) {
		StackTraceElement[] stackTraceElements = failure.getStackTrace();
		StackTraceElement stackTraceElement = null;
		String action = null;
		String lineNum = null;
		if(stackTraceElements!=null && stackTraceElements.length>0){
			stackTraceElement = stackTraceElements[0];
		}
		if(stackTraceElement != null){
			 action = stackTraceElement.getClassName() + "|" + stackTraceElement.getMethodName();
			 lineNum = stackTraceElement.getLineNumber()+"";
		}
		AugeFailureAnalysis failureAnalysis = new AugeFailureAnalysis(failure.getMessage(),action,failure);
		failureAnalysis.setParameters(invokeParameters);
		failureAnalysis.setBizType(bizType);
		failureAnalysis.setLineNum(lineNum);
		failureAnalysis.setFatal(isFatal(failure));
		failureAnalysis.setBusinessException(isBusinessException(failure));
		return failureAnalysis;
	}
	
	
	
	
	
	
}
