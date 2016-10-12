package com.taobao.cun.auge.alilang;

import java.util.List;

public interface AlilangHomePageService {

	List<AlilangTopicDto> getTopics();
	
	UserExamProfile getUnFinishExamCount(Long taobaoUserId);
	
	UserProfile getUserProfile(Long taobaoUserId);
	
	
}
