package com.taobao.cun.auge.asset.aspect;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.client.exception.DefaultServiceException;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 17/5/15.
 */
@Aspect
@Order
@Component
public class AssetServiceExceptionAspect {

    private static final Logger logger = LoggerFactory.getLogger(AssetServiceExceptionAspect.class);

    private static final String MSG = "ASSET_ERROR";

    @AfterThrowing(pointcut = "within(com.taobao.cun.auge.asset..*)", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Exception ex) throws Exception {
        String clazz = joinPoint.getSignature().getDeclaringType().getCanonicalName();
        String name = joinPoint.getSignature().getName();
        String busiKeyword = clazz + "|" + name;
        String parameters = getParameters(joinPoint);
        logger.error(MSG + "|" + busiKeyword + "|Exception|" + parameters, ex);
        if (ex instanceof NullPointerException || ex instanceof AugeBusinessException) {
            throw new DefaultServiceException(ex.getMessage());
        }
        throw new DefaultServiceException(ex.getMessage(), ex.getCause());
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
