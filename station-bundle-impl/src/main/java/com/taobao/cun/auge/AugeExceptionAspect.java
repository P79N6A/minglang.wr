package com.taobao.cun.auge;

import java.util.Map;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

import com.taobao.cun.auge.station.exception.AugeServiceException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Aspect
@Order
@Component
public class AugeExceptionAspect {

    private static final Logger logger = LoggerFactory.getLogger(AugeExceptionAspect.class);

    //@Autowired
    //private DiamondConfiguredProperties configuredProperties;

    @AfterThrowing(pointcut = "within(com.taobao.cun.auge..*ServiceImpl)", throwing = "ex")
    public void handleAugeException(JoinPoint joinPoint, Exception ex) throws Exception {
        String clazz = joinPoint.getSignature().getDeclaringType().getCanonicalName();
        String name = joinPoint.getSignature().getName();
        String action = clazz + "|" + name;
        String parameters = getParameters(joinPoint);
        if (!((ex instanceof AugeBusinessException) || ex instanceof AugeServiceException)) {
            logger.error("{bizType},{action},{parameter}", "augeError", action, parameters, ex);
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

    //private String buildErrorOwner(String name) {
    //	String msg = "other";
    //	for (Map.Entry<String, String> entry : configuredProperties.getExceptionRegularMap().entrySet()) {
    //		if (find(entry.getKey(), name)) {
    //			msg = entry.getValue();
    //			break;
    //		}
    //	}
    //	return msg;
    //}
    //
    //private boolean find(String p, String str) {
    //	Pattern r = Pattern.compile(p);
    //	return r.matcher(str).find();
    //}

}
