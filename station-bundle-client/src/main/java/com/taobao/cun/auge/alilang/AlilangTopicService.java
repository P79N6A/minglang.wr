package com.taobao.cun.auge.alilang;

import java.util.List;

public interface AlilangTopicService {

	List<AlilangTopicDto> getTopics();
	
	void saveAndPublishTopic(List<AlilangTopicDto> topics);
	
}
