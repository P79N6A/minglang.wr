package com.taobao.cun.auge.station.check.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Created by xiao on 18/11/5.
 */
@Component
public class CheckerFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CheckerFactory.applicationContext = applicationContext;
    }

    public static <T> List<T> getCheckerList(Class<T> type) {
        Map<String, T> beansOfType = applicationContext.getBeansOfType(type);
        if (!CollectionUtils.isEmpty(beansOfType)) {
            return (List<T>)beansOfType.values();
        }
        return null;
    }

}

