package com.taobao.cun.auge.lifecycle.statemachine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface StateMachineComponent {
    /**
     * 对应状态及的action
     * @return
     */
    String actionKey();

    /**
     * 状态机
     * @return
     */
    String[] stateMachine();
}
