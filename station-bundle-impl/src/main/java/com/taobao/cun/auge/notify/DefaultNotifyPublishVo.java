package com.taobao.cun.auge.notify;

import java.io.Serializable;

public  class DefaultNotifyPublishVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String topic;
	private String messageType;
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	
}
