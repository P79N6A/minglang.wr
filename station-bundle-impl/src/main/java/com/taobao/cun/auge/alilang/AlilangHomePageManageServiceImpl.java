package com.taobao.cun.auge.alilang;

import java.util.List;

import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;

//@Service("alilangHomePageManageService")
@HSFProvider(serviceInterface= AlilangHomePageManageService.class)
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

	@Override
	public AlilangTopicDto getTopicById(Long id) {
		return alilangBO.getTopicById(id);
	}

	@Override
	public void updateTopic(AlilangTopicDto topic) {
		alilangBO.updateTopic(topic);
	}

}
