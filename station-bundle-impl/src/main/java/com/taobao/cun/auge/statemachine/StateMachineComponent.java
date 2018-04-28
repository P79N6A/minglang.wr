package com.taobao.cun.auge.statemachine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(StateMachineConfiguration.class)
@Component
public @interface StateMachineComponent {
    String actionKey();
    String[] stateMachine();
}
