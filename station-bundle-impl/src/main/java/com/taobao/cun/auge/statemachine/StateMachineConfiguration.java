package com.taobao.cun.auge.statemachine;

import java.util.Map;

import com.alibaba.shared.xfsm.core.context.StateMachineCreateContext;

import com.google.common.collect.Maps;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.StandardAnnotationMetadata;


@Configuration
public class StateMachineConfiguration implements ApplicationListener<ApplicationReadyEvent> {
    @SuppressWarnings("rawtypes")
    private Map<String, Map<String, Class>> actionMappings = Maps.newConcurrentMap();

    private ApplicationContext applicationContext;

    @SuppressWarnings("rawtypes")
    private void setActionMapping(Object bean) {
        StandardAnnotationMetadata standardAnnotationMetadata = new StandardAnnotationMetadata(AopUtils.getTargetClass(bean), true);

        AnnotationAttributes attributets = AnnotationAttributes.fromMap(standardAnnotationMetadata.getAnnotationAttributes(StateMachineComponent.class.getName()));
        String[] stateMachines = attributets.getStringArray("stateMachine");
        for (String stateMachine : stateMachines) {
            Map<String, Class> actionMapping = actionMappings.get(stateMachine);
            if (!applicationContext.containsBean(stateMachine)) {
                throw new RuntimeException("can not find stateMachine By " + stateMachine);
            }
            if (actionMapping == null) {
                actionMapping = Maps.newConcurrentMap();
                actionMapping.put(attributets.getString("actionKey"), AopUtils.getTargetClass(bean));
                actionMappings.put(stateMachine, actionMapping);
            } else {
                if (actionMapping.containsKey(attributets.getString("actionKey"))) {
                    throw new RuntimeException("conflict action key[" + attributets.getString("actionKey") + "] in stateMachine[" + stateMachine + "]");
                }
                actionMapping.put(attributets.getString("actionKey"), AopUtils.getTargetClass(bean));
            }
        }
    }


    private void bindActions(StateMachineCreateContext stateMachineCreateContext) {
        stateMachineCreateContext.setActions(actionMappings.get(stateMachineCreateContext.getId()));
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.applicationContext = event.getApplicationContext();
        Map<String, Object> beansWithAnnotation = event.getApplicationContext().getBeansWithAnnotation(StateMachineComponent.class);
        beansWithAnnotation.values().forEach(this::setActionMapping);
        //所有状态机共用Action
        Map<String, StateMachineCreateContext> beansOfType = event.getApplicationContext().getBeansOfType(StateMachineCreateContext.class);
        beansOfType.values().forEach(this::bindActions);

    }
}
