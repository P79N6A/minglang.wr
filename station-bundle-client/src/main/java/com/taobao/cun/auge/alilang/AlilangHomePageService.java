package com.taobao.cun.auge.alilang;

import java.util.List;

import com.taobao.cun.auge.alilang.dto.AlilangProfileDto;

public interface AlilangHomePageService {

	List<AlilangTopicDto> getTopics();
	
	UserExamProfile getUserExamProfile(Long taobaoUserId);
	
	UserProfile getUserProfile(Long taobaoUserId);

	AlilangProfileDto getAlilangProfile(Long taobaoUserId);
	
	UserProfile getUserProfileByAlilangUserId(String alilangUserId);
	
	List<UserProfile> queryUserForMeeting(String name,Long taobaoUserId);
}
