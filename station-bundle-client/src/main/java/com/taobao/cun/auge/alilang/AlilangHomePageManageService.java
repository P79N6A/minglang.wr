package com.taobao.cun.auge.alilang;

import java.util.List;

public interface AlilangHomePageManageService {

	List<AlilangTopicDto> getTopics();
	
	void saveAndPublishTopic(List<AlilangTopicDto> topics);
	
	void saveTopic(List<AlilangTopicDto> topics);
	
	void publishTopic();
}
