package com.taobao.cun.auge.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class MailConfiguredProperties {

	@Value("#{'${addressUpdateNotifyMailList}'.split(',')}")
	private List<String> addressUpdateNotifyMailList;
	
	@Value("${addressUpdateNotifyMailTemplateId}")
	private String addressUpdateNotifyMailTemplateId;
	
	@Value("${addressUpdateNotifyMailSourceId}")
	private String addressUpdateNotifyMailSourceId;
	
	@Value("${addressUpdateNotifyMailMessageTypeId}")
	private String addressUpdateNotifyMailMessageTypeId;

	public List<String> getAddressUpdateNotifyMailList() {
		return addressUpdateNotifyMailList;
	}

	public void setAddressUpdateNotifyMailList(List<String> addressUpdateNotifyMailList) {
		this.addressUpdateNotifyMailList = addressUpdateNotifyMailList;
	}

	public String getAddressUpdateNotifyMailTemplateId() {
		return addressUpdateNotifyMailTemplateId;
	}

	public void setAddressUpdateNotifyMailTemplateId(String addressUpdateNotifyMailTemplateId) {
		this.addressUpdateNotifyMailTemplateId = addressUpdateNotifyMailTemplateId;
	}

	public String getAddressUpdateNotifyMailSourceId() {
		return addressUpdateNotifyMailSourceId;
	}

	public void setAddressUpdateNotifyMailSourceId(String addressUpdateNotifyMailSourceId) {
		this.addressUpdateNotifyMailSourceId = addressUpdateNotifyMailSourceId;
	}

	public String getAddressUpdateNotifyMailMessageTypeId() {
		return addressUpdateNotifyMailMessageTypeId;
	}

	public void setAddressUpdateNotifyMailMessageTypeId(String addressUpdateNotifyMailMessageTypeId) {
		this.addressUpdateNotifyMailMessageTypeId = addressUpdateNotifyMailMessageTypeId;
	}
	
}
