package com.taobao.cun.auge.failure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalysisReporter;

public class AugeFailureAnalysisReporter implements FailureAnalysisReporter{

	private static final Logger logger = LoggerFactory.getLogger(AugeFailureAnalysisReporter.class);
	@Override
	public void report(FailureAnalysis analysis) {
		if(analysis == null) return;
		if("error".equals(analysis.getAction())){
			logger.error(analysis.getDescription(),analysis.getCause());
		}else if("warn".equals(analysis.getAction())){
			logger.warn(analysis.getDescription(),analysis.getCause());
		}
	}

}
