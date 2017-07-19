package com.taobao.cun.auge;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.failure.AugeFailureAnalysis;
import com.taobao.cun.auge.failure.AugeFailureAnalysisReporter;
import com.taobao.cun.auge.failure.AugeFailureAnalyzer;
import com.taobao.cun.auge.failure.AugeFailureConfiguration;
import com.taobao.cun.auge.station.exception.AugeSystemException;


@Aspect
@Order
@Component
public class AugeExceptionAspect {


    @Autowired
    private AugeFailureConfiguration augeFailureConfiguration;
    
    
    private AugeFailureAnalysisReporter augeFailureAnalysisReporter = new AugeFailureAnalysisReporter();
    
    @AfterThrowing(pointcut = "within(com.taobao.cun.auge..*ServiceImpl)", throwing = "ex")
    public void handleAugeException(JoinPoint joinPoint, Exception ex) throws Exception {
        String parameters = getParameters(joinPoint);
        AugeFailureAnalyzer augeFailureAnalyzer = new AugeFailureAnalyzer(augeFailureConfiguration);
        AugeFailureAnalysis failureAnalysis = (AugeFailureAnalysis) augeFailureAnalyzer.analyze(ex,parameters,"augeError");
        if(failureAnalysis != null){
        	augeFailureAnalysisReporter.report(failureAnalysis);
        	if(!failureAnalysis.isBusinessException()){
        		 throw new AugeSystemException(failureAnalysis.getDescription(),ex);
        	}
        }
    }

    private String getParameters(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            return JSON.toJSONString(args);
        } catch (Exception e) {
            return "parse parameter error";
        }
    }
  
}
