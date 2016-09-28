package com.taobao.cun.diamond;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.boot.diamond.annotation.DiamondConfigListener;
import com.alibaba.boot.diamond.annotation.DiamondListener;
import com.alibaba.boot.diamond.enums.DiamondConfigFormat;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.alilang.AlilangTopicBO;
import com.taobao.cun.auge.alilang.AlilangTopicDto;
import com.taobao.cun.auge.alilang.bo.AlilangTopicBOImpl;
import com.taobao.diamond.client.Diamond;

@DiamondListener
public class AlilangDiamondConfiguration {

	@Autowired
	private AlilangTopicBO alilangBO;
	
	private static final Logger logger = LoggerFactory.getLogger(AlilangTopicBOImpl.class);
	
	@PostConstruct
	public void initTopics(){
		try {
			String alilangTopic = Diamond.getConfig(AlilangTopicBO.ALILANG_TOPIC_DATAID, "DEFAULT_GROUP", 2000);
			receive(alilangTopic);
		} catch (IOException e) {
			logger.error("initTopics error!",e);
		}
	}
	
	@DiamondConfigListener(dataId = AlilangTopicBO.ALILANG_TOPIC_DATAID, ignoredMissingData = true, format = DiamondConfigFormat.TEXT)
	 public void receive(String alilangTopic) {
		List<AlilangTopicDto> topics = JSON.parseArray(alilangTopic, AlilangTopicDto.class);
		alilangBO.receiveTopics(topics);
	 }

}
