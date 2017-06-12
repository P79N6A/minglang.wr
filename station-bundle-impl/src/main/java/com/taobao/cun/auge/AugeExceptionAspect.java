package com.taobao.cun.auge;

import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.client.exception.DefaultServiceException;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 17/5/15.
 */
@Aspect
@Order
@Component
public class AugeExceptionAspect {

    private static final Logger logger = LoggerFactory.getLogger(AugeExceptionAspect.class);

    @Autowired
    private DiamondConfiguredProperties configuredProperties;

    @AfterThrowing(pointcut = "within(com.taobao.cun.auge..*ServiceImpl)", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Exception ex) throws Exception {
        Class declaringType = joinPoint.getSignature().getDeclaringType();
        String clazz = declaringType.getCanonicalName();
        String name = joinPoint.getSignature().getName();
        String simpleName = declaringType.getSimpleName();
        String action = clazz + "|" + name;
        String parameters = getParameters(joinPoint);
        logger.error("{bizType},{action},{parameter}", buildErrorOwner(simpleName), action, parameters, ex);
        throw ex;
    }

    private String getParameters(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            return JSON.toJSONString(args);
        } catch (Exception e) {
            return "parse parameter error";
        }
    }

    private String buildErrorOwner(String simpleName) {
        String msg = "OTHER";
        for (Entry<String, String> entry : configuredProperties.getExceptionRegularMap().entrySet()) {
            if (find(entry.getKey(), simpleName)) {
                msg = entry.getValue();
                break;
            }
        }
        if (configuredProperties.getExceptionFullMap().containsKey(simpleName)) {
            msg = configuredProperties.getExceptionFullMap().get(simpleName);
        }
        return msg;
    }

    private boolean find(String p, String str) {
        Pattern r = Pattern.compile(p);
        return r.matcher(str).find();
    }

}
