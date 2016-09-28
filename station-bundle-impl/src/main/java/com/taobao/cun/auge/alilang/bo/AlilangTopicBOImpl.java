package com.taobao.cun.auge.alilang.bo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.alilang.AlilangTopicBO;
import com.taobao.cun.auge.alilang.AlilangTopicDto;
import com.taobao.cun.auge.dal.domain.AlilangTopic;
import com.taobao.cun.auge.dal.mapper.AlilangTopicMapper;
import com.taobao.diamond.client.Diamond;

@Component("alilangTopicBO")
public class AlilangTopicBOImpl implements AlilangTopicBO {

	private List<AlilangTopicDto> topics;
	
	//private static final Logger logger = LoggerFactory.getLogger(AlilangTopicBOImpl.class);
	
	private static BeanCopier copy = BeanCopier.create(AlilangTopicDto.class, AlilangTopic.class, false);
	
	private AlilangTopicMapper alilangTopicMapper;
	@Override
	public void receiveTopics(List<AlilangTopicDto> alilangTopics) {
		this.topics = alilangTopics;
	}


	@Override
	public void publishTopics(List<AlilangTopicDto> alilangTopics) {
		String content = JSON.toJSONString(alilangTopics);
		Diamond.publishSingle(ALILANG_TOPIC_DATAID, ALILANG_TOPIC_GROUP, content);
	}


	@Override
	public void saveTopics(List<AlilangTopicDto> alilangTopics) {
		alilangTopics.stream().forEach(topicDto -> {
			if(topicDto.getId() != null){
				AlilangTopic topic  = new AlilangTopic();
				copy.copy(topicDto, topic, null);
				alilangTopicMapper.updateByPrimaryKeySelective(topic);
			}else{
				AlilangTopic topic  = new AlilangTopic();
				copy.copy(topicDto, topic, null);
				alilangTopicMapper.insertSelective(topic);
			}
		});
		
	}


	public List<AlilangTopicDto> getTopics() {
		return topics;
	}

}
