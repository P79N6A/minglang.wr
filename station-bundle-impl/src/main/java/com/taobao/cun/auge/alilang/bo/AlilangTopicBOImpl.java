package com.taobao.cun.auge.alilang.bo;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.alilang.AlilangTopicBO;
import com.taobao.cun.auge.alilang.AlilangTopicDto;
import com.taobao.cun.auge.dal.domain.AlilangTopic;
import com.taobao.cun.auge.dal.domain.AlilangTopicExample;
import com.taobao.cun.auge.dal.mapper.AlilangTopicMapper;
import com.taobao.diamond.client.Diamond;

@Component("alilangTopicBO")
public class AlilangTopicBOImpl implements AlilangTopicBO {

	
	private static BeanCopier copy = BeanCopier.create(AlilangTopicDto.class, AlilangTopic.class, false);
	
	private static BeanCopier reverseCopy = BeanCopier.create(AlilangTopic.class, AlilangTopicDto.class, false);
	
	@Autowired
	private AlilangTopicMapper alilangTopicMapper;

	private static final Logger logger = LoggerFactory.getLogger(AlilangTopicBOImpl.class);
	
	@Override
	public void publishTopics(List<AlilangTopicDto> alilangTopics) {
		String content = JSON.toJSONString(alilangTopics);
		boolean isSuccess = Diamond.publishSingle(ALILANG_TOPIC_DATAID, ALILANG_TOPIC_GROUP, content);
		System.out.println(isSuccess);
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


	@Override
    public List<AlilangTopicDto> getTopics() {
		try {
			String alilangTopic = Diamond.getConfig(AlilangTopicBO.ALILANG_TOPIC_DATAID, AlilangTopicBO.ALILANG_TOPIC_GROUP, 2000);
			if(StringUtils.isNotEmpty(alilangTopic)){
				return JSON.parseArray(alilangTopic, AlilangTopicDto.class);
			}
		} catch (IOException e) {
			logger.error("getTopics error!",e);
		}
		return Lists.newArrayList();
	}


	@Override
	public List<AlilangTopicDto> getPersistenceTopic() {
		return alilangTopicMapper.selectByExample(new AlilangTopicExample()).stream().map(topic -> {
			AlilangTopicDto dto = new AlilangTopicDto();
			reverseCopy.copy(topic, dto, null);
			return dto;
		}).collect(Collectors.toList());
	}


	@Override
	public AlilangTopicDto getTopicById(Long id) {
		AlilangTopic topic = alilangTopicMapper.selectByPrimaryKey(id);
		AlilangTopicDto dto = new AlilangTopicDto();
	    reverseCopy.copy(topic, dto, null);
	    return dto;
	}


	@Override
	public void updateTopic(AlilangTopicDto topicDto) {
		Assert.notNull(topicDto.getId());
		AlilangTopic topic =  new AlilangTopic();
		copy.copy(topicDto, topic, null);
		alilangTopicMapper.updateByPrimaryKeySelective(topic);
	}

}
