package com.taobao.cun.auge.alilang;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("alilangHomePageService")
@HSFProvider(serviceInterface= AlilangHomePageService.class)
public  class AlilangHomePageServiceImpl implements AlilangHomePageService {

	
	@Autowired
	private AlilangTopicBO alilangBO;
	@Autowired
	private ExamUserDispatchService examUserDispatchService;
	@Override
	public List<AlilangTopicDto> getTopics() {
		return alilangBO.getTopics();
	}



	public Integer getUnJoinExamCount(Long taobaoUserId){
		return examUserDispatchService.queryUserExamCal(taobaoUserId).getResult().getUnJoinExamNums();
	}




   
}
