package com.taobao.cun.auge.alilang;

import com.taobao.cun.auge.alilang.dto.AlilangProfileDto;

import java.util.List;

public interface AlilangHomePageService {

	List<AlilangTopicDto> getTopics();
	
	UserExamProfile getUserExamProfile(Long taobaoUserId);
	
	UserProfile getUserProfile(Long taobaoUserId);

	AlilangProfileDto getAlilangProfile(Long taobaoUserId);
	
	
}
