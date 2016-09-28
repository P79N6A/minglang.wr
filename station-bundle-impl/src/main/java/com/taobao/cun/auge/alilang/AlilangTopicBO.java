package com.taobao.cun.auge.alilang;

import java.util.List;

public interface AlilangTopicBO {

	 public static final String ALILANG_TOPIC_DATAID = "com.taobao.cun:alilangTopic.json";
	 
	 public static final String ALILANG_TOPIC_GROUP = "DEFAULT_GROUP";
	 
	 void receiveTopics(List<AlilangTopicDto> alilangTopics);
	 
	 void publishTopics(List<AlilangTopicDto> alilangTopics);
	 
	 void saveTopics(List<AlilangTopicDto> alilangTopics);
	 
	 List<AlilangTopicDto> getTopics();
	 
	 List<AlilangTopicDto> getPersistenceTopic();
}
