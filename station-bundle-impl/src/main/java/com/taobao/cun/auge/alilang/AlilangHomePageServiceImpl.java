package com.taobao.cun.auge.alilang;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("alilangHomePageService")
@HSFProvider(serviceInterface= AlilangHomePageService.class)
public  class AlilangHomePageServiceImpl implements AlilangHomePageService {

	
	@Autowired
	private AlilangTopicBO alilangBO;
	
	@Override
	public List<AlilangTopicDto> getTopics() {
		return alilangBO.getTopics();
	}







   
}
