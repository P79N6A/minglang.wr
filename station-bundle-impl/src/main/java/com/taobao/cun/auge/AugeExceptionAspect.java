package com.taobao.cun.auge;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.failure.AugeFailureAnalysis;
import com.taobao.cun.auge.failure.AugeFailureAnalysisReporter;
import com.taobao.cun.auge.failure.AugeFailureAnalyzer;
import com.taobao.cun.auge.failure.AugeFailureConfiguration;
import com.taobao.cun.auge.station.exception.AugeSystemException;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Aspect
@Order
@Component
public class AugeExceptionAspect {


    @Autowired
    private AugeFailureConfiguration augeFailureConfiguration;
    
    
    private AugeFailureAnalysisReporter augeFailureAnalysisReporter = new AugeFailureAnalysisReporter();
    
    @AfterThrowing(pointcut = "within(com.taobao.cun.auge..*ServiceImpl)", throwing = "ex")
    public void handleAugeException(JoinPoint joinPoint, Exception ex) throws Exception {
    	if(ex instanceof ConstraintViolationException){
			ConstraintViolationException cex = (ConstraintViolationException) ex;
			List<String> errors = Lists.newArrayList();

			Set<ConstraintViolation<?>> set = cex.getConstraintViolations();
			for (ConstraintViolation<?> c : set) {
				errors.add(c.getMessage());
			}
			//不需要抛出堆栈信息
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(Joiner.on(";").join(errors), null);
			illegalArgumentException.setStackTrace(new StackTraceElement[]{});
			throw illegalArgumentException;
		}
    	
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
