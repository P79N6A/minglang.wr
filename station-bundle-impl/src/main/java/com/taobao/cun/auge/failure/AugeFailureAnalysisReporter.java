package com.taobao.cun.auge.failure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalysisReporter;
import org.springframework.stereotype.Component;

@Component
public class AugeFailureAnalysisReporter implements FailureAnalysisReporter{

	private static final Logger logger = LoggerFactory.getLogger(AugeFailureAnalysisReporter.class);
	@Override
	public void report(FailureAnalysis analysis) {
		if(analysis == null) return;
         AugeFailureAnalysis augeFailureAnalysis = (AugeFailureAnalysis)analysis;
         if(augeFailureAnalysis.isFatal()){
        	 logger.error("{bizType},{action},{parameter}", augeFailureAnalysis.getBizType(), augeFailureAnalysis.getAction(), augeFailureAnalysis.getParameters(), augeFailureAnalysis.getCause());
         }
	}

	
}
