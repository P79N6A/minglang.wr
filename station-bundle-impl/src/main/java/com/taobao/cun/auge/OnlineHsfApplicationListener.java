package com.taobao.cun.auge;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.alibaba.springframework.boot.util.env.EnvironmentUtil;

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
				HttpClient client =  new HttpClient();  
				GetMethod method = new GetMethod(HSF_ONLINE_URL);
				client.executeMethod(method);  
		        String response = method.getResponseBodyAsString();  				
				System.out.println("－－－－－－－－－－－－online hsf: " + response);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
