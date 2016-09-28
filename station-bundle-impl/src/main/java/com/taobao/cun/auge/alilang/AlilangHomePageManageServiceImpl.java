package com.taobao.cun.auge.alilang;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class AlilangHomePageManageServiceImpl implements AlilangHomePageManageService {

	@Autowired
	private AlilangTopicBO alilangBO;
	
	@Override
	public List<AlilangTopicDto> getTopics() {
		return alilangBO.getPersistenceTopic();
	}

	@Override
	public void saveAndPublishTopic(List<AlilangTopicDto> topics) {
		alilangBO.saveTopics(topics);
		alilangBO.publishTopics(alilangBO.getPersistenceTopic());
	}

	@Override
	public void saveTopic(List<AlilangTopicDto> topics) {
		alilangBO.saveTopics(topics);
	}

	@Override
	public void publishTopic() {
		alilangBO.publishTopics(alilangBO.getPersistenceTopic());
	}

}
