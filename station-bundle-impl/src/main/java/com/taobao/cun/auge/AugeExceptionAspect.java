package com.taobao.cun.auge;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.station.exception.AugeBusinessException;


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
        if (!((ex instanceof AugeBusinessException))) {
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
