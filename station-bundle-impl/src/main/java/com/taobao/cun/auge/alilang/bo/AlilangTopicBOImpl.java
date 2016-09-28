package com.taobao.cun.auge.alilang.bo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.alilang.AlilangTopicBO;
import com.taobao.cun.auge.alilang.AlilangTopicDto;
import com.taobao.cun.auge.dal.domain.AlilangTopic;
import com.taobao.cun.auge.dal.domain.AlilangTopicExample;
import com.taobao.cun.auge.dal.mapper.AlilangTopicMapper;
import com.taobao.diamond.client.Diamond;

@Component("alilangTopicBO")
public class AlilangTopicBOImpl implements AlilangTopicBO {

	private List<AlilangTopicDto> topics;
	
	//private static final Logger logger = LoggerFactory.getLogger(AlilangTopicBOImpl.class);
	
	private static BeanCopier copy = BeanCopier.create(AlilangTopicDto.class, AlilangTopic.class, false);
	
	private static BeanCopier reverseCopy = BeanCopier.create(AlilangTopic.class, AlilangTopicDto.class, false);
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


	@Override
	public List<AlilangTopicDto> getPersistenceTopic() {
		return alilangTopicMapper.selectByExample(new AlilangTopicExample()).stream().map(topic -> {
			AlilangTopicDto dto = new AlilangTopicDto();
			reverseCopy.copy(topic, dto, null);
			return dto;
		}).collect(Collectors.toList());
	}

}
