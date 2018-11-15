package com.taobao.cun.auge.station.check.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    private static final Map<Class, List<Object>> MAP = new HashMap<>(2);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CheckerFactory.applicationContext = applicationContext;
    }

    public static <T> List<T> getCheckerList(Class<T> type) {
        if (MAP.get(type) == null) {
            synchronized (CheckerFactory.class) {
                if (MAP.get(type) == null) {
                    Map<String, T> beansOfType = applicationContext.getBeansOfType(type);
                    if (CollectionUtils.isEmpty(beansOfType)) {
                        MAP.put(type, Collections.emptyList());
                    } else {
                        MAP.put(type, new ArrayList<>(beansOfType.values()));
                    }
                }
            }
        }
        return (List<T>)MAP.get(type);
    }

}

