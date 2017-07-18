package com.taobao.cun.auge.failure;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
@Component
@RefreshScope
public class AugeFailureConfiguration {

	@Value("#{'${ignoreBusinessErrorCodes}'.split(',')}")
	private List<String> ignoreBusinessErrorCodes;

	public List<String> getIgnoreBusinessErrorCodes() {
		return ignoreBusinessErrorCodes;
	}

	public void setIgnoreBusinessErrorCodes(List<String> ignoreBusinessErrorCodes) {
		this.ignoreBusinessErrorCodes = ignoreBusinessErrorCodes;
	}

}
