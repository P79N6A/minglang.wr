package com.taobao.cun.auge.lifecycle;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.taobao.cun.auge.statemachine.StateMachineEvent;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Phase {

	StateMachineEvent event();
	
	String type();
	
	
}
