package com.taobao.cun.auge;

import java.util.HashMap;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.alibaba.springframework.boot.util.env.EnvironmentUtil;
import com.taobao.cun.auge.common.utils.HttpClientUtil;

/**
 * 日常和本地测试环境自动上线hsf
 * 
 * @author linjianke
 *
 */
@Component
public class OnlineHsfApplicationListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

	private static final String HSF_ONLINE_URL = "http://localhost:8080/hsf/online";

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
			String env = EnvironmentUtil.getEnvironment().name().toLowerCase();
			if ("daily".equals(env)) {
				String response = HttpClientUtil.get(HSF_ONLINE_URL, new HashMap<String, String>(), null);
				System.out.println("daily env auto hsf online status: " + response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
