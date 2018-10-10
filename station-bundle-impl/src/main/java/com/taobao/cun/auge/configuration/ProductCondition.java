package com.taobao.cun.auge.configuration;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.alibaba.fastjson.JSON;
import com.taobao.diamond.client.Diamond;

/**
 * 判定环境的Condition，如果是生产环境、预发环境则返回true
 * 
 * @author chengyu.zhoucy
 *
 */
public class ProductCondition implements Condition {

	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String json = null;
		try {
			json = Diamond.getConfig("com.taobao.cun:auge:env", "DEFAULT_GROUP", 5000);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Map<String, String> map = JSON.parseObject(json, Map.class);
		String mode = map.get("mode");
		return mode != null && mode.equals("product");
	}

}
