package com.taobao.cun.auge.configuration;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class TpaGmvCheckConfiguration implements Serializable{
	
	private static final long serialVersionUID = 2851670673426340073L;
	
	@Value("${order.limit.4.auto.close}")
	private Long orderLimit4AutoClose = 10l;

	public Long getOrderLimit4AutoClose() {
		return orderLimit4AutoClose;
	}

	public void setOrderLimit4AutoClose(Long orderLimit4AutoClose) {
		this.orderLimit4AutoClose = orderLimit4AutoClose;
	}
	
	
}
