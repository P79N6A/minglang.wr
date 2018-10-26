package com.taobao.cun.auge.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标注
 * @author chengyu.zhoucy
 *
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface Tag {
	/**
	 * 标注的内容
	 * @return
	 */
	String[] value();
}
