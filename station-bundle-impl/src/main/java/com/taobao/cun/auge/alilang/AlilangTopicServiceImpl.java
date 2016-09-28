package com.taobao.cun.auge.alilang;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("alilangService")
@HSFProvider(serviceInterface= AlilangTopicService.class)
public class AlilangTopicServiceImpl implements AlilangTopicService {

	private List<AlilangTopicDto> topics = Lists.newArrayList();
	
	@Autowired
	private AlilangTopicBO alilangBO;
	
	@Override
	public List<AlilangTopicDto> getTopics() {
		return alilangBO.getTopics();
	}

	@Override
	public void saveAndPublishTopic(List<AlilangTopicDto> topics) {
		alilangBO.saveTopics(topics);
		alilangBO.publishTopics(topics);
	}





   
}
