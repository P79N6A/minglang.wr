package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 16/11/30.
 */
@Component
@RefreshScope
public class DiamondConfiguredProperties {

    @Value("${wisdom.audit.apply}")
    private String apply;

    @Value("${wisdom.audit.pass}")
    private String pass;

    @Value("${wisdom.audit.fail}")
    private String fail;

    @Value("${wisdom.audit.mobile}")
    private String mobile;

    public String getApply() {
        return apply;
    }

    public String getPass() {
        return pass;
    }

    public String getFail() {
        return fail;
    }

    public String getMobile() {
        return mobile;
    }
    
	@Value("${order.limit.4.auto.close}")
	private Long orderLimit4AutoClose = 10l;

	public Long getOrderLimit4AutoClose() {
		return orderLimit4AutoClose;
	}

	public void setOrderLimit4AutoClose(Long orderLimit4AutoClose) {
		this.orderLimit4AutoClose = orderLimit4AutoClose;
	}
	

}
